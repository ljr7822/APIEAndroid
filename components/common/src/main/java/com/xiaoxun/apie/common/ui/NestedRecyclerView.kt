package com.xiaoxun.apie.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NestedRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private var lastY: Float = 0f

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录初始触摸位置
                lastY = e.y
                // 请求父 View 不要拦截触摸事件
                parent?.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaY = e.y - lastY
                lastY = e.y

                // 检查子 RecyclerView 是否可以滚动
                val canScrollUp = canScrollVertically(-1)
                val canScrollDown = canScrollVertically(1)

                if ((!canScrollUp && deltaY > 0) || (!canScrollDown && deltaY < 0)) {
                    // 如果子 RecyclerView 无法滚动，则允许父 View 拦截
                    parent?.requestDisallowInterceptTouchEvent(false)
                } else {
                    // 子 RecyclerView 可以滚动，不允许父 View 拦截
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.onInterceptTouchEvent(e)
    }

}