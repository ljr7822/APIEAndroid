package com.xiaoxun.apie.account_manager.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface AccountDao {
    /** 插入一条用户数据 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(draft: AccountRecord)

    /** 查询一条用户全部数据 */
    @Query("SELECT * FROM account_data WHERE user_id = :userId")
    fun findAccountDataRecord(userId: String): Single<AccountRecord>

    /** 查询指定用户的 token */
    @Query("SELECT token FROM account_data WHERE user_id = :userId")
    fun findAccountTokenByUserId(userId: String): Single<String>

    /** 查询指定用户的 token */
    @Query("SELECT token FROM account_data WHERE phone_number = :phoneNumber")
    fun findAccountTokenByPhoneNum(phoneNumber: String): Single<String>
}