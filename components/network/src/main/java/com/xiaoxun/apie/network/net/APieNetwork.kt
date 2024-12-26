package com.xiaoxun.apie.network.net

import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.utils.account.AccountManager
import com.xiaoxun.apie.network.net.config.ClientConfig
import com.xiaoxun.apie.network.net.factory.APieGsonFactory
import com.xiaoxun.apie.network.net.factory.OkHttpClientFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object APieNetwork {
    private var retrofit: Retrofit? = null

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClientFactory.getOkHttpClient(
            ClientConfig(customInterceptor = { chain ->
                val original = chain.request()
                val builder = original.newBuilder()
                val token = AccountManager.getToken()
                if (token.isNotEmpty()) {
                    builder.addHeader("token", token)
                }
                builder.addHeader("Content-Type", "application/json")
                chain.proceed(builder.build())
            })
        )
    }

    fun getRetrofit(): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(APieConfig.API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(APieGsonFactory.gson))
                .build().also { retrofit = it }
        }
    }
}