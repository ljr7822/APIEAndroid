package com.xiaoxun.apie.common.utils

import android.app.Activity

object AndroidUtils {
    fun isActivityContextReady(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }
}