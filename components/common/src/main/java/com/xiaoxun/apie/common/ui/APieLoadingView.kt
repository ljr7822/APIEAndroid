package com.xiaoxun.apie.common.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.dpF

class APieLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //圆弧半径
    private var radius = 8.dpF

    //圆弧偏移角
    private val offsetAngle = 15f

    //下圆弧起点
    private val startOfDownArc = 180f - offsetAngle

    //上圆弧起点
    private val startOfUpArc = 0f - offsetAngle

    //圆弧旋转度数
    private val rotateAngle = 360f

    // 画笔宽度
    private var paintWidth = 5f

    // 画笔颜色
    private var paintColor: Int = context.getColor(R.color.apie_color_white)

    // 动画时长
    private var animDuration = 1000L

    //圆弧长度
    private val sweepAngle = 180f - 2 * offsetAngle

    private val path = Path()

    private val animation = LoadingAnimation()

    private val paint = Paint()

    private var currentAngleOfDownArc = startOfDownArc
    private var currentAngleOfUpArc = startOfUpArc
    private var finalSweepAngle = sweepAngle

    init {
        // 初始化自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.APieLoadingView)
        radius = typedArray.getDimension(R.styleable.APieLoadingView_loading_view_radius, 8.dpF)
        paintWidth = typedArray.getDimension(R.styleable.APieLoadingView_loading_view_paint_width, 5f)
        paintColor = typedArray.getColor(R.styleable.APieLoadingView_loading_view_color, context.getColor(R.color.apie_color_white))
        animDuration = typedArray.getInt(R.styleable.APieLoadingView_loading_view_anim_duration, 1000).toLong()
        typedArray.recycle()
        initPaint()
        initAnimation()
        //初始化时强制不可见，因为希望可见是通过手动调用show后显示并开始动画
        hide()
    }

    private fun initPaint() {
        paint.apply {
            style = Paint.Style.STROKE
            color = paintColor
            strokeWidth = paintWidth
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
            pathEffect = CornerPathEffect(strokeWidth)
        }
    }

    fun setLoadingColor(color: Int) {
        paintColor = color
        paint.color = color
    }

    private fun initAnimation() {
        animation.apply {
            duration = animDuration
            repeatCount = Animation.INFINITE
            interpolator = LinearInterpolator()
        }
    }

    override fun onDetachedFromWindow() {
        hide()
        super.onDetachedFromWindow()
    }

    fun hide() {
        this.visibility = GONE
        clearAnimation()
    }

    fun show() {
        this.visibility = VISIBLE
        startAnimation(animation)
    }

    fun reset() {
        currentAngleOfDownArc = startOfDownArc
        currentAngleOfUpArc = startOfUpArc
        finalSweepAngle = sweepAngle
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw down arc
        path.reset()
        path.addArc(
            width / 2f - radius + paintWidth,
            height / 2f - radius + paintWidth,
            width / 2f + radius - paintWidth,
            height / 2f + radius - paintWidth,
            currentAngleOfDownArc,
            -1 * finalSweepAngle
        )
        canvas.drawPath(path, paint)

        // Draw up arc
        path.reset()
        path.addArc(
            width / 2f - radius + paintWidth,
            height / 2f - radius + paintWidth,
            width / 2f + radius - paintWidth,
            height / 2f + radius - paintWidth,
            currentAngleOfUpArc,
            -1 * finalSweepAngle
        )
        canvas.drawPath(path, paint)
    }

    inner class LoadingAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            //一次动画转两圈，开始到一圈位置 逐渐减短 一圈到两圈位置 逐渐变长
            if (interpolatedTime > 0.5f) {
                //逐渐变长
                currentAngleOfDownArc = startOfDownArc + rotateAngle * (interpolatedTime - 0.5f) * 2
                currentAngleOfUpArc = startOfUpArc + rotateAngle * (interpolatedTime - 0.5f) * 2
                finalSweepAngle = sweepAngle * (interpolatedTime - 0.5f) * 2
            } else {
                //逐渐减短
                currentAngleOfDownArc = startOfDownArc + rotateAngle * interpolatedTime * 2
                currentAngleOfUpArc = startOfUpArc + rotateAngle * interpolatedTime * 2
                finalSweepAngle = sweepAngle - sweepAngle * interpolatedTime * 2
            }
            invalidate()
        }
    }
}
