package com.xiaoxun.apie.data_loader.repository.cache

import com.xiaoxun.apie.common.db.APieDatabaseHolder
import com.xiaoxun.apie.data_loader.repository.db.DataCacheDBConfig
import com.xiaoxun.apie.data_loader.repository.db.DataCacheDatabase
import io.reactivex.Observable

class DBDataCacheRepository : DataCacheRepository {

    init {
        APieDatabaseHolder.initDb<DataCacheDatabase>(DataCacheManager.getContext(), DataCacheDBConfig())
    }

    override fun findDataCacheByID(subDir: String, dataType: String, timeout: Long): Observable<DataCacheDescriptor> {
        return APieDatabaseHolder
            .getInstance(DataCacheDatabase::class.java)
            .dataCacheDao()
            .findDataCacheRecord(dataType)
            .map {
                DataCacheDescriptor(dataType = dataType, data = it.data, extra = it.extra)
            }
            .toObservable()
    }


    override fun insertDataCache(data: DataCacheDescriptor) {
        val record = DataCacheRecord()
        record.data = data.data
        record.extra = data.extra
        record.timestamp = System.currentTimeMillis()
        record.dateType = data.dataType
        APieDatabaseHolder
            .getInstance(DataCacheDatabase::class.java)
            .dataCacheDao()
            .insert(record)
    }
}