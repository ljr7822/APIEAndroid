package com.xiaoxun.apie.common.db

import android.database.Cursor
import android.database.sqlite.SQLiteException
import androidx.room.DatabaseConfiguration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteQuery

@SuppressWarnings("unused")
abstract class APieDatabase: RoomDatabase() {
    override fun init(configuration: DatabaseConfiguration) {
        try {
            super.init(configuration)
        } catch (e: SQLiteException) {
            throw SQLiteException(e.message, e.cause)
        }
    }

    override fun query(query: SupportSQLiteQuery): Cursor {
        try {
            return super.query(query)
        } catch (e: SQLiteException) {
            throw SQLiteException(e.message, e.cause)
        }
    }

    override fun query(query: String, args: Array<Any?>?): Cursor {
        try {
            return super.query(query, args)
        } catch (e: SQLiteException) {
            throw SQLiteException(e.message, e.cause)
        }
    }
}