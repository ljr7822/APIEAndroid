package com.xiaoxun.apie.common.utils.toast

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.base.APieUtilsCenter
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.ThreadUtil

object APieToast {
    /**
     * Default time interval for messages with the same content.
     */
    private const val DEFAULT_SAME_TEXT_TIME_INTERVAL = 1000L

    private var mToast: Toast? = null
    private var defaultText: String = ""
    private var previousText: String = ""
    private var lastTime: Long = 0

    private const val TAG = "ToastDialog"

    fun showDialog(resId: Int) {
        showDialog(APieUtilsCenter.getApp().getString(resId))
    }

    fun showDialog(text: String, vararg args: Any) {
        showDialog(String.format(text, *args))
    }

    /**
     * This method has a default delay.
     */
    fun showDialog(text: String) {
        showDialog(text, Toast.LENGTH_SHORT)
    }

    fun showDialog(resId: Int, duration: Int) {
        showDialog(APieUtilsCenter.getApp().getString(resId), duration)
    }

    fun showDialog(text: String, duration: Int) {
        showDialog(text, duration, 0, 0)
    }

    fun showDialog(text: String, duration: Int, xOff: Int, yOff: Int) {
        if (TextUtils.isEmpty(text)) return

        val realText = changeTechString(text)
        ThreadUtil.runOnMainThread {
            try {
                if (isSameText(realText)) return@runOnMainThread
                createToast(APieUtilsCenter.getApp(), realText, duration)
            } catch (e: Exception) {
                APieLog.e(TAG, e.message ?: "Unknown error")
            }
        }
    }

    fun showDialog(context: Context, text: String, duration: Int) {
        showDialog(context, text, duration, 0, 0)
    }

    fun showDialog(context: Context, text: String, duration: Int, xOff: Int, yOff: Int) {
        if (TextUtils.isEmpty(text)) return

        val realText = changeTechString(text)

        if (isSameText(realText)) return

        ThreadUtil.runOnMainThread {
            try {
                createToast(context, realText, duration)
            } catch (e: Exception) {
                APieLog.e(TAG, e.message ?: "Unknown error")
            }
        }
    }

    fun showDialogIgnoreOnBackground(context: Context, text: String, duration: Int) {
        if (TextUtils.isEmpty(text)) return

        val realText = changeTechString(text)

        if (isSameText(realText)) return

        ThreadUtil.runOnMainThread {
            try {
                createToast(context, realText, duration)
            } catch (e: Exception) {
                APieLog.e(TAG, e.message ?: "Unknown error")
            }
        }
    }

    private fun createToast(context: Context, text: String, duration: Int) {
        mToast?.cancel()

        val contentView = LayoutInflater.from(context).inflate(R.layout.toast_text_black, null)
        val toastTv: TextView = contentView.findViewById(R.id.toast_text)
        toastTv.text = text

        mToast = ToastCompat.makeText(context, defaultText, duration).apply {
            view = contentView
            setGravity(Gravity.TOP, 0, 0)
            show()
        }
    }

    fun dismiss() {
        mToast?.cancel()
    }

    /**
     * Checks if the text is the same as the previous one within a short time.
     */
    private fun isSameText(text: String): Boolean {
        APieLog.i(TAG, "text is $text")

        return if (text != previousText) {
            previousText = text
            lastTime = System.currentTimeMillis()
            false
        } else {
            val now = System.currentTimeMillis()
            if (now - lastTime < DEFAULT_SAME_TEXT_TIME_INTERVAL) {
                true
            } else {
                lastTime = now
                false
            }
        }
    }

    private fun changeTechString(text: String): String {
        APieLog.i(TAG, "originalText == $text")

        return when {
            text.startsWith("Use JsonReader.set") || text.startsWith("Unable to resolve host") -> "网络错误，请检查网络后重试"
            text.startsWith("无效参数") -> "操作失败"
            else -> text
        }
    }
}