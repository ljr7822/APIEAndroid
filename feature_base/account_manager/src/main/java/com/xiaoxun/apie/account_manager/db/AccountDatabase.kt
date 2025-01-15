package com.xiaoxun.apie.account_manager.db

import androidx.room.Database
import com.xiaoxun.apie.common.db.APieDatabase

@Database(entities = [AccountRecord::class], version = 2, exportSchema = false)
abstract class AccountDatabase: APieDatabase() {
    abstract fun accountDataDao(): AccountDao
}