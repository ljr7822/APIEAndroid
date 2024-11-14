package com.xiaoxun.apie.common.db

import androidx.room.migration.Migration

abstract class APieDatabaseConfig {
    /**
     * @return database name, if empty, will use memory cache.
     */
    open fun configDatabaseName(): String {
        return ""
    }

    /**
     * @return tables migrations.
     */
    open fun migrations(): Array<Migration>? {
        return null
    }

    /**
     * @return database operation if or not run in main thread.
     */
    open fun allowedMainThread(): Boolean {
        return false
    }

    /**
     * @return cipher database pass if use sql cipher database.
     */
    open fun passphrase(): ByteArray? {
        return null
    }

    /**
     * @return use WAL mode or not.
     */
    open fun setWALEnabled(): Boolean {
        return false
    }

    /**
     *
     * @return database class
     */
    abstract fun databaseClass(): Class<*>?

    open fun allowMainThreadQueries(): Boolean {
        return false
    }
}