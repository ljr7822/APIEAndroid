package com.xiaoxun.apie.data_loader.utils

object DataLoaderConfig {
    private var loggerDelegate: LoggerDelegate? = null
    private var cacheType: com.xiaoxun.apie.data_loader.repository.cache.CacheType = com.xiaoxun.apie.data_loader.repository.cache.CacheType.DB

    fun setLoggerDelegate(logger: LoggerDelegate) {
        loggerDelegate = logger
    }

    fun loggerDelegate(): LoggerDelegate? {
        return loggerDelegate
    }

    fun setCacheType(type: com.xiaoxun.apie.data_loader.repository.cache.CacheType) {
        cacheType = type
    }

    fun cacheType(): com.xiaoxun.apie.data_loader.repository.cache.CacheType {
        return cacheType
    }
}