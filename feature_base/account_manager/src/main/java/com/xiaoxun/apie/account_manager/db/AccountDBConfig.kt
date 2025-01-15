package com.xiaoxun.apie.account_manager.db

import androidx.room.migration.Migration
import com.xiaoxun.apie.common.db.APieDatabaseConfig
import com.xiaoxun.apie.common.db.APieDbMigrations

private const val ACCOUNT_DB_NAME = "apie_account_data.db"
private const val ACCOUNT_DB_PHRASE = "apie_account_data"

class AccountDBConfig : APieDatabaseConfig() {
    private fun migration1To2() = object : APieDbMigrations(1, 2) {
        override fun migrate(): Array<String> {
            return arrayOf("ALTER TABLE account_data ADD COLUMN grade INTEGER DEFAULT 0 NOT NULL",)
        }
    }

    override fun databaseClass(): Class<*> {
        return AccountDatabase::class.java
    }

    override fun configDatabaseName(): String {
        return ACCOUNT_DB_NAME
    }

    override fun migrations(): Array<Migration> {
        return arrayOf(
            migration1To2(),
        )
    }

    override fun passphrase(): ByteArray? {
//        return ACCOUNT_DB_PHRASE.toByteArray()
        return null
    }
}