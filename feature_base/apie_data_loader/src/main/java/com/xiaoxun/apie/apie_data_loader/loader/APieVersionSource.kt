package com.xiaoxun.apie.apie_data_loader.loader

import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.data_loader.repository.cache.CacheType
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.utils.DataLoaderConfig
import io.reactivex.Observable
import com.xiaoxun.apie.data_loader.utils.DataLoaderLogger
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheDescriptor
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheRepository

interface APieVersionSource {
    fun <Data> getVersion(params: APieRequestParams<Data>): Observable<String>
    fun <Data> saveVersion(version: String, params: APieRequestParams<Data>)
}

class APieLocalVersionSource(private val cacheManager: DataCacheManager) : APieVersionSource {
    override fun <Data> getVersion(params: APieRequestParams<Data>): Observable<String> {
        return cacheRepository(DataLoaderConfig.cacheType())
            .findDataCacheByID(params.subDir(), params.dataType() + "_version")
            .map {
                it.data
            }.onErrorReturn {
                "0"
            }
    }

    override fun <Data> saveVersion(version: String, params: APieRequestParams<Data>) {
        DataLoaderLogger.i("${params.url()} save version is $version")
        val dataCacheDescriptor = DataCacheDescriptor(
            subDir = params.subDir(),
            dataType = params.dataType() + "_version",
            data = version,
            extra = params.url()
        )
        cacheRepository(DataLoaderConfig.cacheType()).insertDataCache(dataCacheDescriptor)
    }

    private fun cacheRepository(cacheType: CacheType): DataCacheRepository {
        return when (cacheType) {
            CacheType.DB -> cacheManager.dbCache()
            CacheType.SP -> cacheManager.spCache()
            CacheType.FILE -> cacheManager.fileCache()
        }
    }
}