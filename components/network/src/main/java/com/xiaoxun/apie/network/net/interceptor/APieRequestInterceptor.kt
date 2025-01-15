package com.xiaoxun.apie.network.net.interceptor

import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.network.net.intf.IAPieNetReqInfo
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class APieRequestInterceptor(private val iNetworkRequiredInfo: IAPieNetReqInfo) : Interceptor {
    /**
     * Intercepts the network request
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //val nowDateTime = DateUtil.getDateTime()
        // Build a new request
        val builder = chain.request().newBuilder()
        val token = AccountManager.getToken()
        if (token.isNotEmpty()) {
            builder.addHeader("token", token)
        }
        val userId = AccountManager.getUserId()
        if (userId.isNotEmpty()) {
            builder.addHeader("userId", userId)
        }
        builder.addHeader("Content-Type", "application/json")
        // Add environment information
        builder.addHeader("os", "android")
        // Add device model
        iNetworkRequiredInfo.getDeviceModel()?.let { builder.addHeader("device_model", it) }
        // Add OS version
        iNetworkRequiredInfo.getOsVersion()?.let { builder.addHeader("os_version", it) }
        // Add app version code
        iNetworkRequiredInfo.getAppVersionCode()?.let { builder.addHeader("appVersionCode",
            it.toString()
        ) }
        // Add app version name
        iNetworkRequiredInfo.getAppVersionName()?.let { builder.addHeader("appVersionName", it) }
        // Add date and time
        //builder.addHeader("datetime", nowDateTime)
        // Proceed with the request
        return chain.proceed(builder.build())
    }
}