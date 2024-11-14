package com.xiaoxun.apie.data_loader.repository.db

import androidx.room.Database
import com.xiaoxun.apie.common.db.APieDatabase
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheRecord

@Database(entities = [DataCacheRecord::class], version = 1, exportSchema = false)
abstract class DataCacheDatabase: APieDatabase() {
    abstract fun dataCacheDao(): DataCacheDao
}