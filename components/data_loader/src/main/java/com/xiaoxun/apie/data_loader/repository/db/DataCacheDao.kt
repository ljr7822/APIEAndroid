package com.xiaoxun.apie.data_loader.repository.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheRecord
import io.reactivex.Single

@Dao
interface DataCacheDao {
    /** 插入一条缓存数据 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: DataCacheRecord)

    /** 查询一条缓存数据 */
    @Query("SELECT * FROM data_cache WHERE date_type = :dataType")
    fun findDataCacheRecord(dataType: String): Single<DataCacheRecord>
}