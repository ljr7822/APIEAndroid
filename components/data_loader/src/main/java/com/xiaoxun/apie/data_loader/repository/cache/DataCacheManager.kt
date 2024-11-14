package com.xiaoxun.apie.data_loader.repository.cache

import android.content.Context
import android.content.SharedPreferences
import com.xiaoxun.apie.data_loader.repository.store.FileDataCacheRepository
import com.xiaoxun.apie.data_loader.repository.store.MemoryDataCacheStore
import com.xiaoxun.apie.data_loader.repository.store.SPDataCacheRepository

/**
 * 负责内存缓存管理
 */
class DataCacheManager(private val context: Context) {
    private var dbCache: DBDataCacheRepository? = null
    private var fileCache: FileDataCacheRepository? = null
    private var spCache: SPDataCacheRepository? = null
    private var memoryCache: MemoryDataCacheStore? = null


    fun dbCache(): DataCacheRepository {
        return dbCache ?: DBDataCacheRepository().also {
            dbCache = it
        }
    }

    fun memoryCache(): MemoryDataCacheStore {
        return memoryCache ?: MemoryDataCacheStore().also {
            memoryCache = it
        }
    }

    fun fileCache(): DataCacheRepository {
        return fileCache ?: FileDataCacheRepository().also {
            fileCache = it
        }
    }

    fun spCache(): SPDataCacheRepository {
        return spCache ?: SPDataCacheRepository().also {
            spCache = it
        }
    }


    companion object {
        private lateinit var config: DataCacheConfig
        private lateinit var context: Context
        private const val NAME_SP = "data_cache"

        fun init(ctx: Context, dataConfig: DataCacheConfig) {
            config = dataConfig
            context = ctx
        }

        private fun checkInit() {
            require(Companion::config.isInitialized) { "please call init first" }
            require(Companion::context.isInitialized) { "please call init first" }
        }

        internal fun getFileCacheDir(): String {
            checkInit()
            return config.getFileCacheDir()
        }


        internal fun getFileCacheName(dataType: String): String {
            checkInit()
            return config.getFileCacheName(dataType)
        }

        internal fun getSPCacheKey(dataType: String): String {
            checkInit()
            return config.getSPCacheKey(dataType)
        }

        internal fun getSPCache(): SharedPreferences {
            checkInit()
            return context.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE)
        }

        internal fun getContext(): Context {
            checkInit()
            return context
        }
    }
}

interface DataCacheConfig {
    fun getFileCacheDir(): String
    fun getSPCacheKey(dataType: String): String
    fun getFileCacheName(dataType: String): String
}