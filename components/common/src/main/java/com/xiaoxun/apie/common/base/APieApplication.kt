package com.xiaoxun.apie.common.base

import android.app.Application
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.ForegroundCallbacks
import com.xiaoxun.apie.common.utils.intf.OnAppStatusListener

class APieApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(ForegroundCallbacks(object : OnAppStatusListener {
            override fun onForeground() {
                APieLog.d("ForegroundCallbacks", "正在前台")
            }

            override fun onBackground() {
                APieLog.d("ForegroundCallbacks", "进入后台")
            }
        }))
    }
}