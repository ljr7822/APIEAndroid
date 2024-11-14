package com.xiaoxun.apie.network.net.config

import com.xiaoxun.apie.network.net.interceptor.APieRequestInterceptor
import com.xiaoxun.apie.network.net.interceptor.APieResponseInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 构建okhttpClient的配置
 */
data class ClientConfig(
    val connectTimeout: Long = 10,
    val writeTimeout: Long = 10,
    val readTimeout: Long = 20,
    val requestInterceptor: APieRequestInterceptor? = null,
    val responseInterceptor: APieResponseInterceptor? = null,
    val loggingInterceptor: HttpLoggingInterceptor? = null,
    val customInterceptor: ((Interceptor.Chain) -> Response)? = null
)
