package com.xiaoxun.apie.common.utils

import android.app.Activity
import android.content.Context
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object StatusBarUtils {

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context?): Int {
        if (context == null) {
            return 0
        }
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取状态栏的高度（单位：像素）
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val decorView = activity.window.decorView
        val insets = ViewCompat.getRootWindowInsets(decorView)
        return insets?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
    }

    /**
     * 检查状态栏是否可见
     */
    fun isStatusBarVisible(activity: Activity): Boolean {
        val decorView = activity.window.decorView
        val insets = ViewCompat.getRootWindowInsets(decorView)
        return insets?.isVisible(WindowInsetsCompat.Type.statusBars()) ?: true
    }
}