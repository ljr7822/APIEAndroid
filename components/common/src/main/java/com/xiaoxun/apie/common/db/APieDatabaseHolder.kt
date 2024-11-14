package com.xiaoxun.apie.common.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.ConcurrentHashMap

/**
 * 管理 APic 数据库实例
 */
object APieDatabaseHolder {

    private val map: ConcurrentHashMap<Class<*>, RoomDatabase> = ConcurrentHashMap()

    @Suppress("UNCHECKED_CAST")
    fun <T : RoomDatabase> getInstance(tClass: Class<T>): T {
        // 尝试从缓存中获取数据库实例，如果不存在则抛出异常
        return map[tClass] as? T ?: throw IllegalStateException("Database for ${tClass.simpleName} is not initialized. Please call initDb() first.")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : RoomDatabase> initDb(context: Context, config: APieDatabaseConfig) {
        // 首先尝试获取已存在的数据库实例
        var db = map[config.databaseClass() as Class<T>] as? T
        // 如果实例不存在或者数据库未打开，则创建一个新的数据库实例
        if (db == null || !db.isOpen) {
            val builder = Room.databaseBuilder(context, config.databaseClass() as Class<T>, config.configDatabaseName())
            builder.fallbackToDestructiveMigration()
            builder.fallbackToDestructiveMigrationFrom()

            if (config.allowedMainThread()) {
                builder.allowMainThreadQueries()
            }

            config.migrations()?.let {
                builder.addMigrations(*it)
            }

            builder.setJournalMode(if (config.setWALEnabled()) RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING else RoomDatabase.JournalMode.TRUNCATE)

            if (config.allowMainThreadQueries()) {
                builder.allowMainThreadQueries()
            }
            // 构建数据库实例
            db = builder.build()

            // 更新缓存
            map[config.databaseClass() as Class<T>] = db
        }
    }

    fun <T : RoomDatabase> registerObserver(tClass: Class<T>, observer: APieObserver) {
        val db = getInstance(tClass)
        db.invalidationTracker.addObserver(observer)
    }

    fun <T : RoomDatabase> removeObserver(tClass: Class<T>, observer: APieObserver) {
        val db = getInstance(tClass)
        db.invalidationTracker.removeObserver(observer)
    }

    fun <T : RoomDatabase> releaseDb(tClass: Class<T>) {
        val db = getInstance(tClass)
        db.close()
        map.remove(tClass)
    }
}