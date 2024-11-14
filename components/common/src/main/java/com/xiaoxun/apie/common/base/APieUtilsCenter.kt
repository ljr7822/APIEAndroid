package com.xiaoxun.apie.common.base

import android.app.Application

object APieUtilsCenter {
    @JvmField
    var isDebug = false

    private val application: Application by lazy {
        APieBaseApplication.application()
    }

    @JvmStatic
    fun getApp(): Application {
        return application
    }

    @JvmField
    var callbackOpt = false
}