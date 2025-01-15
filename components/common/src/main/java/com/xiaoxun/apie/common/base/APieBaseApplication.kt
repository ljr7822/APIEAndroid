package com.xiaoxun.apie.common.base

import android.app.Application
import com.tencent.mmkv.MMKV
import com.xiaoxun.apie.common.utils.sound_pool.APieSoundPoolHelper

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
        APieSoundPoolHelper.init(this)
        val dir = filesDir.absolutePath + "/apie_mmkv"
        MMKV.initialize(this, dir)
    }
}