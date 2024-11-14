package com.xiaoxun.apie.data_loader.repository.source

import com.google.gson.reflect.TypeToken
import com.xiaoxun.apie.common.utils.GsonUtils
import com.xiaoxun.apie.data_loader.repository.cache.CacheType
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheDescriptor
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheRepository
import com.xiaoxun.apie.data_loader.utils.DataLoaderConfig
import com.xiaoxun.apie.data_loader.utils.DataLoaderLogger
import io.reactivex.Observable
import java.lang.reflect.ParameterizedType

class LocalDataSource(private val cacheManager: DataCacheManager) : IDataSource {

    override fun <Data> loadSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return cacheRepository(DataLoaderConfig.cacheType())
            .findDataCacheByID(params.subDir(), params.dataType(), params.cacheTimeout())
            .map {
                val type = (params.javaClass.getGenericSuperclass() as? ParameterizedType)?.actualTypeArguments?.get(0)
                    ?: (params.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0]
                val response = GsonUtils.fromJson<Data>(it.data, type)
                DataLoaderLogger.i("${params.url()} 获取缓存数据 success 缓存类型：${DataLoaderConfig.cacheType()}")
                if (response == null) {
                    throw Exception("缓存数据无效")
                }
                com.xiaoxun.apie.data_loader.data.BaseResponse<Data>()
                    .apply {
                        code = 1
                        data = response
                    }
            }
    }

    private fun cacheRepository(cacheType: CacheType): DataCacheRepository {
        return when (cacheType) {
            CacheType.DB -> cacheManager.dbCache()
            CacheType.SP -> cacheManager.spCache()
            CacheType.FILE -> cacheManager.fileCache()
        }
    }

    fun <Data> cacheData(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>, data: Data) {
        val dataCacheDescriptor = DataCacheDescriptor(
            subDir = params.subDir(),
            dataType = params.dataType(),
            data = GsonUtils.toJson(data, object : TypeToken<Data>() {}.type),
            extra = params.url()
        )
        cacheRepository(DataLoaderConfig.cacheType()).insertDataCache(dataCacheDescriptor)
    }
}