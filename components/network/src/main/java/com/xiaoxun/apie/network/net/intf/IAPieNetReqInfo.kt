package com.xiaoxun.apie.network.net.intf

import android.app.Application

/**
 * 实际的网络请求中会需要打印日志和一些请求时间的显示
 * 在请求网络接口的时候打印当前的App的运行信息
 */
interface IAPieNetReqInfo {
    /**
     * 获取App版本名
     */
    fun getAppVersionName(): String?

    /**
     * 获取App版本号
     */
    fun getAppVersionCode(): Int?

    /**
     * 获取设备型号
     */
    fun getDeviceModel(): String?

    /**
     * 获取操作系统版本
     */
    fun getOsVersion(): String?

    /**
     * 判断是否为Debug模式
     */
    fun isDebug(): Boolean

    /**
     * 获取全局上下文参数
     */
    fun getApplicationContext(): Application?
}