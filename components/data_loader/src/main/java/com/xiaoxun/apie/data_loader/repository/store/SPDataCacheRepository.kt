package com.xiaoxun.apie.data_loader.repository.store

import com.xiaoxun.apie.data_loader.repository.cache.DataCacheDescriptor
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheRepository
import io.reactivex.Observable

class SPDataCacheRepository : DataCacheRepository {
    override fun findDataCacheByID(subDir: String, dataType: String, timeout: Long): Observable<DataCacheDescriptor> {
        val spKey = DataCacheManager.getSPCacheKey(dataType)
        val value = DataCacheManager.getSPCache().getString(spKey, "")
        return if (value.isNullOrEmpty()) {
            Observable.error(Exception("sp缓存数据为空"))
        } else {
            val data = DataCacheDescriptor(dataType = dataType, data = value)
            Observable.just(data)
        }
    }

    override fun insertDataCache(data: DataCacheDescriptor) {
        DataCacheManager.getSPCache()
            .edit()
            .putString(DataCacheManager.getSPCacheKey(data.dataType), data.data)
            .apply()
    }
}