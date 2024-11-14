package com.xiaoxun.apie.network.net.interceptor

import com.xiaoxun.apie.common.utils.APieLog
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class APieResponseInterceptor : Interceptor {

    companion object {
        const val TAG = "APieResponseInterceptor"
    }

    /**
     * Intercepts the response
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        APieLog.i(TAG, "requestSpendTime=${System.currentTimeMillis() - requestTime}ms")
        return response
    }
}