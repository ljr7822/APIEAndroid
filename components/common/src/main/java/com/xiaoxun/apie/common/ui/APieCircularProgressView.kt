package com.xiaoxun.apie.common.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.xiaoxun.apie.common.R

class APieCircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var totalCount = 100
    private var completedCount = 0
    private var progressAngle = 0f
    private var animator: ValueAnimator? = null
    private var animDuration: Long = 300
    private var isCompleted = false
    private val rectF = RectF()
    // 背景圆环画笔颜色
    private var paintBackgroundColor = ContextCompat.getColor(context, R.color.apieTheme_colorBlack_alpha_10)
    // 进度圆环画笔颜色
    private var paintProgressColor = ContextCompat.getColor(context, R.color.apie_color_white)
    // 完成对勾的颜色
    private var paintCheckMarkColor = ContextCompat.getColor(context, R.color.apie_color_97B4FF)
    // 画笔宽度
    private var paintWidth = 10f

    private val paintBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintCheckMark = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private var progressListener: ProgressListener? = null

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.APieCircularProgressView)
        paintWidth = attributes.getDimension(R.styleable.APieCircularProgressView_strokeWidth, 10f)
        animDuration = attributes.getInteger(R.styleable.APieCircularProgressView_animDuration, 300).toLong()
        paintBackgroundColor = attributes.getColor(R.styleable.APieCircularProgressView_paintBackgroundColor, paintBackgroundColor)
        paintProgressColor = attributes.getColor(R.styleable.APieCircularProgressView_paintProgressColor, paintProgressColor)
        paintCheckMarkColor = attributes.getColor(R.styleable.APieCircularProgressView_paintCheckMarkColor, paintCheckMarkColor)
        attributes.recycle()
        initPaint()
    }

    private fun initPaint() {
        paintBackground.strokeWidth = paintWidth
        paintBackground.color = paintBackgroundColor

        paintProgress.strokeWidth = paintWidth
        paintProgress.color = paintProgressColor

        paintCheckMark.strokeWidth = 6f
        paintCheckMark.color = paintCheckMarkColor
    }

    fun setTotalCount(total: Int) {
        totalCount = total
        invalidate()
    }

    fun setCompletedCount(completed: Int) {
        if (completed in 0..totalCount) {
            updateProgress(completed)
        }
    }

    fun incrementCompletedCount() {
        if (completedCount < totalCount) {
            updateProgress(completedCount + 1)
        }
    }

    fun decrementCompletedCount() {
        if (completedCount > 0) {
            updateProgress(completedCount - 1)
        }
    }

    private fun updateProgress(newCompletedCount: Int) {
        val targetAngle = if (totalCount > 0) {
            360f * newCompletedCount / totalCount
        } else {
            0f
        }

        animator?.cancel()
        animator = ValueAnimator.ofFloat(progressAngle, targetAngle).apply {
            duration = animDuration
            addUpdateListener { animation ->
                progressAngle = animation.animatedValue as Float
                invalidate()
            }
            start()
        }

        completedCount = newCompletedCount

        isCompleted = (completedCount == totalCount)
        if (isCompleted) {
            progressListener?.onProgressCompleted()
        } else {
            progressListener?.onProgressChanged(completedCount, totalCount)
        }
    }

    fun setProgressListener(listener: ProgressListener) {
        this.progressListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 确定圆形绘制区域
        val width = width.toFloat()
        val height = height.toFloat()
        val padding = paintProgress.strokeWidth / 2
        rectF.set(padding, padding, width - padding, height - padding)
        // 绘制背景圆环
        canvas.drawArc(rectF, 0f, 360f, false, paintBackground)
        // 改变进度条颜色
        paintProgress.color = if (isCompleted) paintCheckMarkColor else paintProgressColor

        // 绘制进度圆环
        canvas.drawArc(rectF, -90f, progressAngle, false, paintProgress)
        if (isCompleted) {
            // 绘制对勾
            drawCheckMark(canvas, width, height)
        }
    }

    private fun drawCheckMark(canvas: Canvas, width: Float, height: Float) {
        // 调整对勾的坐标比例，使其更小
        val scaleFactor = 0.8f // 对勾的大小比例，相较于原来的大小减半
        val startX = width / 2 - width * scaleFactor / 7
        val startY = height / 2
        val midX = width / 2
        val midY = height / 2 + height * scaleFactor / 7
        val endX = width / 2 + width * scaleFactor / 7
        val endY = height / 2 - height * scaleFactor / 7
        canvas.drawLine(startX, startY, midX, midY, paintCheckMark)
        canvas.drawLine(midX, midY, endX, endY, paintCheckMark)
    }

    interface ProgressListener {
        fun onProgressChanged(completedCount: Int, totalCount: Int)
        fun onProgressCompleted()
    }
}