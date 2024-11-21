package com.xiaoxun.apie.common.utils

import android.util.Log

/**
 * 日志工具类
 */
private const val APIE_TAG = "APie-"
class APieLog {
    companion object {
        fun d(tag: String, msg: String) {
            Log.d("$APIE_TAG$tag", msg)
        }

        fun e(tag: String, msg: String) {
            Log.e("$APIE_TAG$tag", msg)
        }

        fun i(tag: String, msg: String) {
            Log.i("$APIE_TAG$tag", msg)
        }

        fun v(tag: String, msg: String) {
            Log.v("$APIE_TAG$tag", msg)
        }

        fun w(tag: String, msg: String) {
            Log.w("$APIE_TAG$tag", msg)
        }
    }
}