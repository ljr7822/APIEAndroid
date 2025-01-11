package com.xiaoxun.apie.common.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.dpF
import com.xiaoxun.apie.common.utils.sp
import com.xiaoxun.apie.common.utils.spF
import java.math.BigDecimal
import java.math.BigInteger
import java.text.DecimalFormat

class ScrollingDigitalAnimation : androidx.appcompat.widget.AppCompatTextView {

    private var numStart = text.toString()
    private var numEnd: String? = null

    private var duration: Long = 800

    private var prefixString = "" // Prefix string
    private var postfixString = "" // Postfix string

    private var isInt = false  // Whether it's an integer

    // Set the animation duration (default is 2000 ms)
    fun setDuration(duration: Long) {
        this.duration = duration
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setNumberString(number: String) {
        setNumberString("0", number)
    }

    fun setNumberString(numStart: String, numberEnd: String) {
        this.numStart = numStart
        this.numEnd = numberEnd
        if (checkNumString(numStart, numberEnd)) {
            // Valid numbers, start animation
            start()
        } else {
            // Invalid numbers, directly set the final value
            text = "$prefixString$numberEnd$postfixString"
        }
    }

    // Set the prefix string
    fun setPrefixString(prefixString: String) {
        this.prefixString = prefixString
    }

    // Set the postfix string
    fun setPostfixString(postfixString: String) {
        this.postfixString = postfixString
    }

    private fun start() {
        // 创建数字动画，并设置起始值和结束值
        val valueAnimator = ValueAnimator.ofObject(
            BigDecimalEvaluator(),
            BigDecimal(numStart),
            BigDecimal(numEnd)
        )
        valueAnimator.duration = duration
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()

        // 添加动画更新监听
        valueAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as BigDecimal
            text = "$prefixString${format(value)}$postfixString"
        }
        valueAnimator.start()
    }

    // Format BigDecimal to two decimal places if it's a float or to an integer if it's an integer
    private fun format(bd: BigDecimal): String {
        val pattern = if (isInt) {
            "#,###" // Integer format
        } else {
            "#,##0.00" // Decimal format
        }
        val decimalFormat = DecimalFormat(pattern)
        return decimalFormat.format(bd)
    }

    // Calculate linear interpolation result
    class BigDecimalEvaluator : TypeEvaluator<BigDecimal> {
        override fun evaluate(fraction: Float, startValue: BigDecimal, endValue: BigDecimal): BigDecimal {
            // 计算起始值和结束值之间的差异
            val result = endValue.subtract(startValue)
            // 根据 fraction（插值比例）返回计算后的数值
            return startValue.add(result.multiply(BigDecimal(fraction.toString())))
        }
    }

    // Check if the numbers are valid (either integers or decimals)
    private fun checkNumString(numStart: String, numEnd: String): Boolean {
        try {
            BigInteger(numStart)
            BigInteger(numEnd)
            isInt = true
        } catch (ex: Exception) {
            isInt = false
            ex.printStackTrace()
        }

        return try {
            val start = BigDecimal(numStart)
            val end = BigDecimal(numEnd)
            end >= start || end < start
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }
}