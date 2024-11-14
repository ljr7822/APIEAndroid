package com.xiaoxun.apie.network.net.factory

import com.xiaoxun.apie.network.net.config.ClientConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object OkHttpClientFactory {
    fun getOkHttpClient(clientConfig: ClientConfig): OkHttpClient {
        val sSLSocketFactory = OkHttpSSHFactory.createSSLSocketFactory()
        val trustAllCerts = OkHttpSSHFactory.TrustAllCerts()
        val trustAllHostnameVerifier = OkHttpSSHFactory.TrustAllHostnameVerifier()
        return if (sSLSocketFactory != null) {
            OkHttpClient.Builder().apply {
                sslSocketFactory(sSLSocketFactory, trustAllCerts)
                hostnameVerifier(trustAllHostnameVerifier)
                connectTimeout(clientConfig.connectTimeout, TimeUnit.SECONDS)
                writeTimeout(clientConfig.writeTimeout, TimeUnit.SECONDS)
                readTimeout(clientConfig.readTimeout, TimeUnit.SECONDS)
                clientConfig.requestInterceptor?.let { addInterceptor(it) }
                clientConfig.responseInterceptor?.let { addInterceptor(it) }
                clientConfig.loggingInterceptor?.let { addInterceptor(it) }
                clientConfig.customInterceptor?.let { customInterceptor ->
                    addInterceptor { chain ->
                        customInterceptor(chain)
                    }
                }
            }.build()
        } else {
            OkHttpClient.Builder().apply {
                connectTimeout(clientConfig.connectTimeout, TimeUnit.SECONDS)
                writeTimeout(clientConfig.writeTimeout, TimeUnit.SECONDS)
                readTimeout(clientConfig.readTimeout, TimeUnit.SECONDS)
                clientConfig.requestInterceptor?.let { addInterceptor(it) }
                clientConfig.responseInterceptor?.let { addInterceptor(it) }
                clientConfig.loggingInterceptor?.let { addInterceptor(it) }
                clientConfig.customInterceptor?.let { customInterceptor ->
                    addInterceptor { chain ->
                        customInterceptor(chain)
                    }
                }
            }.build()
        }
    }
}