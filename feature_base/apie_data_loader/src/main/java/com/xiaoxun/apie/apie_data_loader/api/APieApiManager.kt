package com.xiaoxun.apie.apie_data_loader.api

import com.xiaoxun.apie.network.net.APieNet
import com.xiaoxun.apie.apie_data_loader.service.APieAccountService

object APieApiManager {
    /**
     * the api service cache, since service can create only once
     */
    private val edithServiceCache: MutableMap<String, Any> = mutableMapOf()

    fun <T> getOrCreate(clazz: Class<T>, context: ApiGateway = ApiGateway.EDITH): T {
        val key = clazz.canonicalName!!
        val cache = edithServiceCache[key]
        return if (cache == null) {
            val value = createService(clazz, context)
            @Suppress("ReplacePutWithAssignment")
            edithServiceCache.put(key, value as Any)
            value
        } else cache as T
    }

    private fun <T> createService(clazz: Class<T>, context: ApiGateway): T {
        return when (context) {
            ApiGateway.EDITH -> APieNet.getService("edit", clazz)
        }
    }

    fun getAccountAPIService(): APieAccountService = getOrCreate(APieAccountService::class.java, ApiGateway.EDITH)
}

sealed class ApiGateway {
    /**
     * 访问edith网关使用的retrofitClient key
     */
    object EDITH : ApiGateway()
}