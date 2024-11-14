package com.xiaoxun.apie.data_loader.repository.cache

import io.reactivex.Observable

interface DataCacheRepository {

    fun findDataCacheByID(subDir: String = "", dataType: String, timeout: Long = 0): Observable<DataCacheDescriptor>

    fun insertDataCache(data: DataCacheDescriptor)
}

data class DataCacheDescriptor(val subDir: String = "", val dataType: String, val extra: String? = null, val data: String)