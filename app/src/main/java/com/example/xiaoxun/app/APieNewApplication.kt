package com.example.xiaoxun.app

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.common.base.APieBaseApplication
import com.xiaoxun.apie.common.intf.OnAppStatusListener
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.FilePathConfig
import com.xiaoxun.apie.common.utils.ForegroundCallbacks
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper
import com.xiaoxun.apie.data_loader.repository.cache.CacheType
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheConfig
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.utils.DataLoaderConfig
import com.xiaoxun.apie.data_loader.utils.LoggerDelegate

class APieNewApplication : APieBaseApplication() {
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

        initDataCache(this)
        initDataLoader()
        initSharedPreferences(this)
        initARouter(this)
    }

    private fun initDataCache(app: Application) {
        DataCacheManager.init(app, object : DataCacheConfig {
            override fun getFileCacheDir(): String {
                return FilePathConfig.DATA_CACHE_DIR
            }

            override fun getSPCacheKey(dataType: String): String {
                return dataType
            }

            override fun getFileCacheName(dataType: String): String {
                return dataType
            }
        })

//        DiskCacheManager.addDiskCacheEntry(
//            FilePathConfig.DATA_CACHE_DIR + File.separator + DataLoaderConstant.STICKER_SUB_DIR,
//            DataLoaderConstant.STICKER_MAX_CACHE_SIZE
//        )
//
//        DiskCacheManager.addDiskCacheEntry(
//            FilePathConfig.DATA_CACHE_DIR + File.separator + DataLoaderConstant.MUSIC_SUB_DIR,
//            DataLoaderConstant.MUSIC_MAX_CACHE_SIZE
//        )
    }

    private fun initDataLoader() {
        DataLoaderConfig.setLoggerDelegate(object : LoggerDelegate {
            override fun e(tag: String, message: String, throwable: Throwable?) {
                APieLog.e(tag, message)
            }

            override fun d(tag: String, message: String) {
                APieLog.d(tag, message)
            }

            override fun i(tag: String, message: String) {
                APieLog.i(tag, message)
            }

            override fun v(tag: String, message: String) {
                APieLog.v(tag, message)
            }
        })
        DataLoaderConfig.setCacheType(CacheType.DB)
        DataLoaderManager.instance.initWithContext(this)
    }

    private fun initSharedPreferences(app: Application) {
        SharedPreferencesHelper.init(app)
    }

    private fun initARouter(app: Application) {
        // TODO: 建立开发模式和发布模式的切换
        ARouter.openLog()   // 打印日志
        ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(app)   // 尽可能早，推荐在Application中初始化
    }
}