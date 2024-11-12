package com.xiaoxun.apie.common.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.xiaoxun.apie.common.utils.intf.OnAppStatusListener

/**
 * 实现方式1：前后台监听
 */
private const val TAG = "ForegroundCallbacks"

class ForegroundCallbacks(private val mOnAppStatusListener: OnAppStatusListener?) :
    Application.ActivityLifecycleCallbacks {

    // 当前Activity数量
    private var activityStartCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        APieLog.d(TAG, "onActivityCreated: $activity")
    }

    override fun onActivityStarted(activity: Activity) {
        APieLog.d(TAG, "onActivityStarted: $activity")
        activityStartCount++
        // 数值从0变到1说明是从后台切到前台
        if (activityStartCount == 1) {
            //从后台切到前台
            mOnAppStatusListener?.onForeground()
        }
    }

    override fun onActivityResumed(activity: Activity) {
        APieLog.d(TAG, "onActivityResumed: $activity")
    }

    override fun onActivityPaused(activity: Activity) {
        APieLog.d(TAG, "onActivityPaused: $activity")
    }

    override fun onActivityStopped(activity: Activity) {
        APieLog.d(TAG, "onActivityStopped: $activity")
        activityStartCount--
        // 数值从1到0说明是从前台切到后台
        if (activityStartCount == 0) {
            // 从前台切到后台
            mOnAppStatusListener?.onBackground()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        APieLog.d(TAG, "onActivitySaveInstanceState: $activity")
    }

    override fun onActivityDestroyed(activity: Activity) {
        APieLog.d(TAG, "onActivityDestroyed: $activity")
    }
}