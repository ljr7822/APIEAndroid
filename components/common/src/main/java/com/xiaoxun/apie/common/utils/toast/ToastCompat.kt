package com.xiaoxun.apie.common.utils.toast

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import java.lang.reflect.Field

class ToastCompat private constructor(
    context: Context,
    @NonNull private val baseToast: Toast
) : Toast(context) {

    companion object {
        /**
         * Make a standard toast that just contains a text view.
         *
         * @param context  The context to use.  Usually your [android.app.Application]
         *                 or [android.app.Activity] object.
         * @param text     The text to show.  Can be formatted text.
         * @param duration How long to display the message.  Either [Toast.LENGTH_SHORT] or
         *                 [Toast.LENGTH_LONG]
         */
        @SuppressLint("ShowToast")
        @JvmStatic
        fun makeText(context: Context, text: CharSequence, duration: Int): ToastCompat {
            val toast = Toast.makeText(context, text, duration)
            setContextCompat(toast.view, SafeToastContext(context, toast))
            return ToastCompat(context, toast)
        }

        /**
         * Make a standard toast that just contains a text view with the text from a resource.
         *
         * @param context  The context to use.  Usually your [android.app.Application]
         *                 or [android.app.Activity] object.
         * @param resId    The resource id of the string resource to use.  Can be formatted text.
         * @param duration How long to display the message.  Either [Toast.LENGTH_SHORT] or
         *                 [Toast.LENGTH_LONG]
         * @throws Resources.NotFoundException if the resource can't be found.
         */
        @JvmStatic
        fun makeText(context: Context, @StringRes resId: Int, duration: Int): ToastCompat {
            val text = context.resources.getText(resId)
            return makeText(context, text, duration)
        }

        private fun setContextCompat(view: View?, context: Context) {
            if (Build.VERSION.SDK_INT == 25 && view != null) {
                try {
                    val field: Field = View::class.java.getDeclaredField("mContext")
                    field.isAccessible = true
                    field.set(view, context)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        }
    }

    fun setBadTokenListener(listener: BadTokenListener): ToastCompat {
        val context = view?.context
        if (context is SafeToastContext) {
            context.setBadTokenListener(listener)
        }
        return this
    }

    override fun show() {
        baseToast.show()
    }

    override fun setDuration(duration: Int) {
        baseToast.duration = duration
    }

    override fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        baseToast.setGravity(gravity, xOffset, yOffset)
    }

    override fun setMargin(horizontalMargin: Float, verticalMargin: Float) {
        baseToast.setMargin(horizontalMargin, verticalMargin)
    }

    override fun setText(resId: Int) {
        baseToast.setText(resId)
    }

    override fun setText(s: CharSequence?) {
        baseToast.setText(s)
    }

    override fun setView(view: View?) {
        baseToast.view = view
        setContextCompat(view, SafeToastContext(view?.context ?: return, this))
    }

    override fun getHorizontalMargin(): Float {
        return baseToast.horizontalMargin
    }

    override fun getVerticalMargin(): Float {
        return baseToast.verticalMargin
    }

    override fun getDuration(): Int {
        return baseToast.duration
    }

    override fun getGravity(): Int {
        return baseToast.gravity
    }

    override fun getXOffset(): Int {
        return baseToast.xOffset
    }

    override fun getYOffset(): Int {
        return baseToast.yOffset
    }

    override fun getView(): View? {
        return baseToast.view
    }

    fun getBaseToast(): Toast {
        return baseToast
    }
}