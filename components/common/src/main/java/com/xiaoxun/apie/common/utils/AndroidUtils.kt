package com.xiaoxun.apie.common.utils

import android.content.Context
import android.text.TextUtils

object AndroidUtils {
    private var sVersionName: String = ""
    private var sVersionCode: Int = 0
    /**
     * 获取版本号
     */
    fun getVersionCode(context: Context): Int {
        try {
            synchronized(AndroidUtils::class.java) {
                if (sVersionCode == 0) {
                    val manager = context.packageManager
                    val info = manager.getPackageInfo(context.packageName, 0)
                    sVersionCode = info.versionCode
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sVersionCode
    }

    /**
     * 获取版本名
     */
    fun getVersionName(context: Context): String {
        try {
            synchronized(AndroidUtils::class.java) {
                if (TextUtils.isEmpty(sVersionName)) {
                    val manager = context.packageManager
                    val info =
                        manager.getPackageInfo(context.packageName, 0)
                    sVersionName = info.versionName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sVersionName
    }
}