package com.xiaoxun.apie.network.net.factory

import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.network.net.config.ClientConfig
import okhttp3.OkHttpClient
import java.io.IOException
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    fun getOkHttpClient(clientConfig: ClientConfig): OkHttpClient {
        val sSLSocketFactory = OkHttpSSHFactory.createSSLSocketFactory()
        val trustAllCerts = OkHttpSSHFactory.TrustAllCerts()
        val trustAllHostnameVerifier = OkHttpSSHFactory.TrustAllHostnameVerifier()

        val builder = OkHttpClient.Builder().apply {
            // SSL 配置
            if (sSLSocketFactory != null) {
                sslSocketFactory(sSLSocketFactory, trustAllCerts)
                hostnameVerifier(trustAllHostnameVerifier)
            }

            // 超时配置
            connectTimeout(clientConfig.connectTimeout, TimeUnit.SECONDS)
            writeTimeout(clientConfig.writeTimeout, TimeUnit.SECONDS)
            readTimeout(clientConfig.readTimeout, TimeUnit.SECONDS)

            // 添加拦截器
            clientConfig.requestInterceptor?.let { addInterceptor(it) }
            clientConfig.responseInterceptor?.let { addInterceptor(it) }
            clientConfig.loggingInterceptor?.let { addInterceptor(it) }

            // 添加自定义拦截器，处理异常
            clientConfig.customInterceptor?.let { customInterceptor ->
                addInterceptor { chain ->
                    try {
                        customInterceptor(chain)
                    } catch (e: IOException) {
                        // 捕获网络异常并显示 Toast
                        APieToast.showDialog("网络连接异常:${e.localizedMessage}")
                        throw e
                    }
                }
            }
        }
        return builder.build()
    }
}