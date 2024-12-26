package com.xiaoxun.apie.common.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.inputmethod.InputMethodManager

object KeyBoardUtils {
    /**
     * 键盘高度
     */
    private var keyboardHeight = 0

    private const val FILE_NAME = "keyboard.info"

    private const val KEY_KEYBOARD_HEIGHT = "sp.key.keyboard.height"
    private const val KEY_KEYBOARD_MAX_HEIGHT = "sp.key.keyboard.maxheight"
    private val DEFAULT_KEYBOARD_HEIGHT = 275.dp


    @JvmStatic
    fun showKeyboard(context: Context?, view: View, force: Boolean = false) {
        context ?: return
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, if(force) InputMethodManager.SHOW_FORCED else InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
        }

    }

    @JvmStatic
    fun showKeyboard(context: Context?) {
        if (context == null) {
            return
        }
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
        }

    }

    @JvmStatic
    fun hideKeyboard(context: Context?) {
        if (context == null) {
            return
        }
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow((context as Activity).window.decorView.windowToken, 0)
        } catch (e: Exception) {
        }

    }

    @JvmStatic
    fun hideKeyboard(context: Context?, windowToken: IBinder?) {
        try {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        } catch (e: Exception) {
        }

    }

    @JvmStatic
    fun hideKeyboard(context: Context, window: Window) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        } catch (e: Exception) {
        }

    }

    /**
     * 点击外部View，隐藏键盘
     *
     * @param event
     * @param outsideView
     * @param exceptViews
     * @param tempRect
     */
    @JvmStatic
    fun hideKeyboardTouchOutside(
        event: MotionEvent, outsideView: View,
        exceptViews: List<View>? = null, tempRect: Rect = Rect()
    ) {
        if (event.action == MotionEvent.ACTION_UP) {
            if (!exceptViews.isNullOrEmpty()) {
                val touchX = event.rawX.toInt()
                val touchY = event.rawY.toInt()
                tempRect.setEmpty()
                exceptViews?.forEach { view ->
                    view.getGlobalVisibleRect(tempRect)
                    if (tempRect.contains(touchX, touchY)) {
                        return
                    }
                }
            }
            hideKeyboard(outsideView.context, outsideView.windowToken)
        }
    }
}

fun View.showKeyboard() {
    val ims = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    if (hasWindowFocus()) {
        requestFocus()
        ims.showSoftInput(this, 0)
    } else {
        viewTreeObserver.addOnWindowFocusChangeListener(object : ViewTreeObserver.OnWindowFocusChangeListener {
            override fun onWindowFocusChanged(hasFocus: Boolean) {
                if (hasFocus) {
                    viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    requestFocus()
                    ims.showSoftInput(this@showKeyboard, 0)
                }
            }
        })
    }
}

fun View.hideKeyboard() {
    val ims = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
    ims.hideSoftInputFromWindow(windowToken, 0)
}