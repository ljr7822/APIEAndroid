package com.xiaoxun.apie.common.ui

import android.content.Context
import android.util.AttributeSet
import com.xiaoxun.apie.common.R
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton


class APieFloatActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val TAG = "ScrollFloatingButton"
    private var mX = 0f
    private var mY = 0f
    private var mParentWidth = 0
    private var mParentHeight = 0
    private var mScrollEnable = true
    private var mScrollLeft = 0
    private var mScrollTop = 0
    private var mRight = 0
    private var mScrollRight = 0
    private var mScrollBottom = 0
    private var hasScroll = false
    private var isScroll = false

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ScrollFloatingButton,
            0, 0
        ).apply {
            try {
                mScrollEnable = getBoolean(R.styleable.ScrollFloatingButton_scrollEnable, true)
            } finally {
                recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Prevent resetting the position of ScrollFloatingButton when the layout is reset
        (parent as? ViewGroup)?.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (hasScroll && mScrollRight != 0 && mScrollBottom != 0) {
                layout(mScrollLeft, mScrollTop, mScrollRight, mScrollBottom)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        (parent as? ViewGroup)?.let {
            mParentWidth = it.width
            mParentHeight = it.height
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!mScrollEnable) return super.onTouchEvent(ev)

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mX = ev.x
                mY = ev.y
                super.onTouchEvent(ev)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
                var x = ev.x - mX
                var y = ev.y - mY

                if (Math.abs(scaledTouchSlop) < Math.abs(x) || Math.abs(scaledTouchSlop) < Math.abs(y)) {
                    isScroll = true
                }

                if (isScroll) {
                    mScrollLeft = (x + left).toInt()
                    mScrollTop = (y + top).toInt()
                    mScrollRight = (x + right).toInt()
                    mScrollBottom = (y + bottom).toInt()

                    // Prevent scrolling out of the parent view
                    if (mScrollLeft < 0 || mScrollRight > mParentWidth) {
                        mScrollLeft = left
                        mScrollRight = right
                    }
                    if (mScrollTop < 0 || mScrollBottom > mParentHeight) {
                        mScrollTop = top
                        mScrollBottom = bottom
                    }

                    layout(mScrollLeft, mScrollTop, mScrollRight, mScrollBottom)
                    hasScroll = true
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isScroll) {
                    isScroll = false
                    isPressed = false // Reset pressed state
                    return true
                }
            }
        }

        return super.onTouchEvent(ev)
    }

    fun setScrollEnable(scrollEnable: Boolean) {
        mScrollEnable = scrollEnable
    }

    fun isScrollEnable(): Boolean {
        return mScrollEnable
    }
}