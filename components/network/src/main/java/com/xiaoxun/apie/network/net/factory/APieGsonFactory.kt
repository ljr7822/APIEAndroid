package com.xiaoxun.apie.network.net.factory

import com.google.gson.ExclusionStrategy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object APieGsonFactory {
    init {
        initAPieFactory()
    }
    // 初始化线程池
    private var mExecutor: Executor? = null

    // 维持一个全局的Gson
    val gson: Gson by lazy {
        // 配置全局gson格式
        GsonBuilder() // 设置时间格式
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") // 设置过滤器，数据库级别的model不进行转换
            .setExclusionStrategies(DBFlowExclusionStrategy())
            .create()
    }

    private fun initAPieFactory() {
        // 新建一个4线程的线程池
        mExecutor = Executors.newFixedThreadPool(4)
    }
}

class DBFlowExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: com.google.gson.FieldAttributes): Boolean {
        // 跳过DBFlow的字段
        return f.declaringClass.toString().contains("com.raizlabs.android.dbflow")
    }

    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }
}