package com.xiaoxun.apie.common.db

import android.database.SQLException
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xiaoxun.apie.common.utils.APieLog

abstract class APieDbMigrations(startVersion: Int, endVersion: Int) : Migration(startVersion, endVersion) {

    /**
     * @return migration SQL array.
     */
    protected abstract fun migrate(): Array<String>

    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            val sqlGroup = migrate()
            for (sql in sqlGroup) {
                database.execSQL(sql)
            }
        } catch (ex: SQLException) {
            APieLog.e("DB","database migrate from version $startVersion to version $endVersion failed")
        }
    }
}
