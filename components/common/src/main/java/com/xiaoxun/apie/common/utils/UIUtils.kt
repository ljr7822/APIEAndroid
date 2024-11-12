package com.xiaoxun.apie.common.utils

import android.content.Context
import android.graphics.drawable.Drawable

object UIUtils {
    /**
     * dip-->px
     */
    fun dip2Px(context: Context, dip: Int): Int {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        val density = context.resources.displayMetrics.density
        val px = (dip * density + 0.5f).toInt()
        return px
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    fun sp2px(context: Context, spValue: Int): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun getColor(context: Context, colorId: Int): Int {
        return context.resources.getColor(colorId)
    }

    fun getDrawable(context: Context, resId: Int): Drawable {
        return context.resources.getDrawable(resId)
    }
}