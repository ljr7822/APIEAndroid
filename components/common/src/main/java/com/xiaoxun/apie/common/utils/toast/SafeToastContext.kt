package com.xiaoxun.apie.common.utils.toast

import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable

class SafeToastContext(
    @NonNull base: Context,
    @NonNull private val toast: Toast
) : ContextWrapper(base) {

    private var badTokenListener: BadTokenListener? = null

    override fun getApplicationContext(): Context {
        return ApplicationContextWrapper(baseContext.applicationContext)
    }

    fun setBadTokenListener(@NonNull badTokenListener: BadTokenListener) {
        this.badTokenListener = badTokenListener
    }

    private inner class ApplicationContextWrapper(@NonNull base: Context) : ContextWrapper(base) {
        override fun getSystemService(@NonNull name: String): Any? {
            return if (Context.WINDOW_SERVICE == name) {
                WindowManagerWrapper(baseContext.getSystemService(name) as WindowManager)
            } else {
                super.getSystemService(name)
            }
        }
    }

    private inner class WindowManagerWrapper(
        @NonNull private val base: WindowManager
    ) : WindowManager {

        override fun getDefaultDisplay(): Display {
            return base.defaultDisplay
        }

        override fun removeViewImmediate(view: View) {
            base.removeViewImmediate(view)
        }

        override fun addView(view: View, params: ViewGroup.LayoutParams) {
            try {
                Log.d(TAG, "WindowManager's addView(view, params) has been hooked.")
                base.addView(view, params)
            } catch (e: WindowManager.BadTokenException) {
                Log.i(TAG, e.message ?: "BadTokenException caught")
                badTokenListener?.onBadTokenCaught(toast)
            } catch (throwable: Throwable) {
                Log.e(TAG, "[addView]", throwable)
            }
        }

        override fun updateViewLayout(view: View, params: ViewGroup.LayoutParams) {
            base.updateViewLayout(view, params)
        }

        override fun removeView(view: View) {
            base.removeView(view)
        }
    }

    companion object {
        private const val TAG = "WindowManagerWrapper"
    }
}

interface BadTokenListener {
    fun onBadTokenCaught(toast: Toast)
}