package com.xiaoxun.apie.data_loader.repository.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 数据缓存表
 */
@Entity(tableName = "data_cache")
class DataCacheRecord {
    @PrimaryKey
    @ColumnInfo(name = "date_type")
    var dateType: String = ""

    @ColumnInfo(name = "data")
    var data: String = ""

    @ColumnInfo(name = "extra_info")
    var extra: String? = ""

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0
}