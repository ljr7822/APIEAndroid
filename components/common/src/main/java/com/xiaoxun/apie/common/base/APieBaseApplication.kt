package com.xiaoxun.apie.common.base

import android.app.Application

abstract class APieBaseApplication: Application() {
    companion object {
        private lateinit var instance: APieBaseApplication

        fun application(): APieBaseApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}