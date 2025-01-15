package com.xiaoxun.apie.common.mmkv

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * Initialize MMKV by App startup. You can cancel the automatic initialization of MMKV.
 * If you want to change root directory or log level, you can add the following code to
 * the AndroidManifest.xml:
 *
 * ```xml
 * <meta-data
 *   android:name="mmkv_root_dir"
 *   android:value="/mmkv_2" />
 *
 * <meta-data
 *   android:name="mmkv_log_level"
 *   android:value="debug" />
 * ```
 *
 * @author Dylan Cai
 */
class MMKVInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val rootDir = context.filesDir.absolutePath + (getMetaData(context, "mmkv_root_dir") ?: "/mmkv")
        val logLevel = when (getMetaData(context, "mmkv_log_level")?.lowercase()) {
            "debug" -> MMKVLogLevel.LevelDebug
            "info" -> MMKVLogLevel.LevelInfo
            "warning" -> MMKVLogLevel.LevelWarning
            "error" -> MMKVLogLevel.LevelError
            "none" -> MMKVLogLevel.LevelNone
            else -> MMKVLogLevel.LevelInfo
        }
        MMKV.initialize(context, rootDir, logLevel)
    }

    private fun getMetaData(context: Context, key: String): String? =
        try {
            context.packageManager.getApplicationInfo(context.packageName, GET_META_DATA)
                .metaData?.getString(key)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}