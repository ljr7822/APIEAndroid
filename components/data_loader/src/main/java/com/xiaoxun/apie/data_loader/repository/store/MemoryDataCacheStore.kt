package com.xiaoxun.apie.data_loader.repository.store

class MemoryDataCacheStore {
    private val dataMap: MutableMap<String, MemoryData<*>> = mutableMapOf()

    fun <T> save(key: String, t: T) {
        dataMap[key] = MemoryData(t, System.currentTimeMillis())
    }

    fun <T> get(key: String): T? {
        val value = dataMap[key] ?: return null
        return value.data as T?
    }
}

data class MemoryData<T>(val data: T, val timestamp: Long, val isExpire: Boolean = false)