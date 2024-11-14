package com.xiaoxun.apie.common.utils

import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * sp to float value
 */
inline val Float.spF: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics)
    }

/**
 * sp to int value
 */
inline val Int.sp: Int get() = toFloat().sp

/**
 * sp to int value
 */
inline val Float.sp: Int get() = spF.toInt()

/**
 * dp to float value
 */
inline val Float.dpF: Float
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics)
    }

/**
 * dp to float value
 */
inline val Int.dpF: Float get() = toFloat().dpF

/**
 * dp to int value
 */
inline val Int.dp: Int get() = toFloat().dp

/**
 * dp to int value
 */
inline val Float.dp: Int get() = dpF.toInt()

/**
 *px2dp
 */
inline val Float.px2dpF: Float get() = this / Resources.getSystem().displayMetrics.density + 0.5f

/**
 *px2dp
 */
inline val Float.px2dp: Int get() = px2dpF.toInt()

/**
 *px2dp
 */
inline val Int.px2dpF: Float get() = toFloat().px2dpF

/**
 *px2dp
 */
inline val Int.px2dp: Int get() = px2dpF.toInt()


fun View?.hide() {
    this?.visibility = View.GONE
}

fun View?.show() {
    this?.visibility = View.VISIBLE
}

/**
 * 当 condition 为 true 时，执行action
 *
 * @param shouldShow
 * @param action
 */
fun <T : View> T?.showIf(shouldShow: Boolean, action: (T.() -> Unit)? = null) {
    this?.visibility = if (shouldShow) View.VISIBLE else View.GONE
    if (shouldShow) action?.invoke(this!!)
}

fun View?.hideIf(shouldHide: Boolean) {
    showIf(!shouldHide)
}

fun View?.invisible() {
    this?.visibility = View.INVISIBLE
}

fun View?.isVisible() = this?.visibility == View.VISIBLE
fun View?.isGone() = this?.visibility == View.GONE
fun View?.isInvisible() = this?.visibility == View.INVISIBLE

fun TextView?.setTextOrHide(text: CharSequence) {
    this?.text = text
    showIf(text.isNotEmpty())
}

fun View?.scaleTouchArea(padding: Float,
                         top: Float = padding,
                         bottom: Float = padding,
                         left: Float = padding,
                         right: Float = padding) {
    (this?.parent as View).run {
        val r = Rect()
        this.getHitRect(r)
        r.top -= top.dp
        r.bottom += bottom.dp
        r.left -= left.dp
        r.right += right.dp
        touchDelegate = TouchDelegate(r, this)
    }
}

/**
 * 设置View的Margin
 */
fun View?.setMargin(marginLeft: Int = 0, marginTop: Int = 0, marginRight: Int = 0, marginBottom: Int = 0) {
    val param = this?.layoutParams ?: return
    if (param is ViewGroup.MarginLayoutParams) {
        param.setMargins(marginLeft, marginTop, marginRight, marginBottom)
    }
    this.layoutParams = param
}

/**
 * 设置View的MarginStart
 *
 * @param marginStart
 */
fun View?.setMarginStart(marginStart: Int) {
    val param = this?.layoutParams ?: return
    if (param is ViewGroup.MarginLayoutParams) {
        param.marginStart = marginStart
    }
    this.layoutParams = param
}

/**
 * 设置View的MarginEnd
 *
 * @param marginEnd
 */
fun View?.setMarginEnd(marginEnd: Int) {
    val param = this?.layoutParams ?: return
    if (param is ViewGroup.MarginLayoutParams) {
        param.marginEnd = marginEnd
    }
    this.layoutParams = param
}

/**
 * view 拓展方法
 *
 * @param left padding值
 */
fun View?.setPaddingLeft(left: Int) {
    this?.setPadding(left, this.paddingTop, this.paddingRight, this.paddingBottom)
}

/**
 * view 拓展方法
 *
 * @param top padding值
 */
fun View?.setPaddingTop(top: Int) {
    this?.setPadding(this.paddingLeft, top, this.paddingRight, this.paddingBottom)
}

/**
 * view 拓展方法
 *
 * @param right padding值
 */
fun View?.setPaddingRight(right: Int) {
    this?.setPadding(this.paddingLeft, this.paddingTop, right, this.paddingBottom)
}

/**
 * view 拓展方法
 *
 * @param bottom padding值
 */
fun View?.setPaddingBottom(bottom: Int) {
    this?.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, bottom)
}