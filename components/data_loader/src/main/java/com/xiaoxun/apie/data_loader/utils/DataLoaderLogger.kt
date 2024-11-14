package com.xiaoxun.apie.data_loader.utils

object DataLoaderLogger {
    private const val TAG = "apie_data_loader"

    fun e(message: String, throwable: Throwable? = null) {
        DataLoaderConfig.loggerDelegate()?.e(TAG, message, throwable)
    }

    fun d(message: String) {
        DataLoaderConfig.loggerDelegate()?.d(TAG, message)
    }

    fun i(message: String) {
        DataLoaderConfig.loggerDelegate()?.i(TAG, message)
    }

    fun v(message: String) {
        DataLoaderConfig.loggerDelegate()?.v(TAG, message)
    }
}

interface LoggerDelegate {
    fun e(tag: String, message: String, throwable: Throwable?)

    fun d(tag: String, message: String)

    fun i(tag: String, message: String)

    fun v(tag: String, message: String)
}
