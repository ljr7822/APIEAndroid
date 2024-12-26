package com.xiaoxun.apie.common.utils

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet

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

/**
 * 用于设置 view 在无障碍模式下读取的 class name;
 * 比如 ImageView  在无障碍模式下，设置 ImageView.setAccessClassNameDelegate(Button::class.java.name) 后，在 读取时会结尾会补充「按钮」二字
 * 默认会读取「按钮」二字
 */
fun View.setAccessClassNameDelegate(delegateClassName: String = Button::class.java.name) {
    this.accessibilityDelegate = object : View.AccessibilityDelegate() {
        override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            info.className = delegateClassName
        }
    }
}

const val DEF_DURATION = 250L
abstract class DebouncingClickListener(private val duration: Long) : OnClickListener {
    private var lastClickTs = 0L
    override fun onClick(v: View) {
        val current = SystemClock.elapsedRealtime()
        if (current - lastClickTs > duration) {
            lastClickTs = current
            onDebouncedClick(v)
        }
    }

    abstract fun onDebouncedClick(v: View)
}

fun View.setDebouncingClickListener(duration: Long = DEF_DURATION, block: (View) -> Unit) {
    this.setOnClickListener(object : DebouncingClickListener(duration) {
        override fun onDebouncedClick(v: View) {
            block(v)
        }
    })
}

@SuppressLint("CodeCommentMethod")
fun View.alphaShow(duration: Long = 250L) {
    if (this.visibility == View.VISIBLE && alpha == 1f) return
    animate().alpha(1f).setDuration(duration)
        .withStartAction { this.show() }
        .start()
}


@SuppressLint("CodeCommentMethod")
fun View.alphaHide(duration: Long = 250L) {
    if (this.visibility != View.VISIBLE) return
    animate().alpha(0f).setDuration(duration)
        .withStartAction { this.show() }
        .withEndAction { this.hide() }
        .start()
}

fun View.animateHeight(targetHeight: Int, duration: Long = 300, onAnimationEnd: (() -> Unit)? = null) {
    val initialHeight = this.height
    val animator = ValueAnimator.ofInt(initialHeight, targetHeight).apply {
        this.duration = duration
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            val layoutParams = this@animateHeight.layoutParams
            layoutParams.height = animatedValue
            this@animateHeight.layoutParams = layoutParams
        }
        addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationEnd(animation: android.animation.Animator) {
                onAnimationEnd?.invoke()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })
    }
    animator.start()
}