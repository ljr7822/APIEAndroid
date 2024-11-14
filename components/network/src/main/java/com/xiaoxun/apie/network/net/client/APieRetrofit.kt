package com.xiaoxun.apie.network.net.client

import okhttp3.HttpUrl
import retrofit2.Retrofit

class APieRetrofit internal constructor(private val retrofit: Retrofit) {

    /**
     * 创建由服务接口定义的 API 端的实现
     *
     * @param T 服务接口类
     * @param service 服务接口的类
     * @return 服务接口的实现
     */
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    /**
     * 获取baseUrl
     */
    fun baseUrl(): HttpUrl {
        return retrofit.baseUrl()
    }
}