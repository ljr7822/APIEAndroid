package com.xiaoxun.apie.data_loader.repository.db

import androidx.room.migration.Migration
import com.xiaoxun.apie.common.db.APieDatabaseConfig
import com.xiaoxun.apie.common.db.APieDbMigrations

private const val NAME_DATA_BASE = "apie_data_cache.db"

class DataCacheDBConfig : APieDatabaseConfig() {

    private fun migration1To2() = object : APieDbMigrations(1, 2) {
        override fun migrate(): Array<String> {
            return arrayOf("ALTER TABLE note_draft ADD COLUMN reason INTEGER NOT NULL DEFAULT 0")
        }
    }

    override fun databaseClass(): Class<*> {
        return DataCacheDatabase::class.java
    }

    override fun configDatabaseName(): String {
        return NAME_DATA_BASE
    }

    override fun migrations(): Array<Migration> {
        return arrayOf(
            migration1To2()
        )
    }
}