package com.xiaoxun.apie.common.navbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.UIUtils

/**
 * 底部导航栏根节点
 */
class APieNavBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), OnPageChangeListener {
    companion object {
        private const val TAG = "APieNavBarLayout"
    }

    private var titleTextBold = false //文字加粗
    private var titleTextSize = 12 //文字大小 默认为12sp
    private var titleNormalColor = 0 //描述文本的默认显示颜色
    private var titleSelectedColor = 0 //述文本的默认选中显示颜色
    private var marginTop = 0 //文字和图标的距离,默认0dp
    private var iconWidth = 0 //图标的宽度
    private var iconHeight = 0 //图标的高度
    private var itemPadding = 0 //BottomBarItem的padding
    private var unreadTextSize = 10 //未读数默认字体大小10sp
    private var unreadNumThreshold = 99 //未读数阈值
    private var unreadTextColor = 0 //未读数字体颜色
    private var unreadTextBg: Drawable? = null //未读数字体背景
    private var msgTextSize = 6 //消息默认字体大小6sp
    private var msgTextColor = 0 //消息文字颜色
    private var msgTextBg: Drawable? = null //消息文字背景
    private var notifyPointBg: Drawable? = null //小红点背景

    private var barBackground: Drawable? = null
    private var barHeight = 45 // bar的高度

    private var floatIcon: Drawable? = null //凸起图标
    private var floatEnable = false //是否中间图标凸起
    private var floatMarginBottom = 0 //凸起按钮底部间距
    private var floatIconWidth = 0 //凸起图标的宽度
    private var floatIconHeight = 0 //凸起图标的高度

    private val mItemViews: MutableList<APieNavBarItem> = ArrayList()
    private var mCurrentItem = 0 //当前条目的索引
    private var mSmoothScroll = false
    private var mSameTabClickCallBack = false

    private var mViewPager2: ViewPager2? = null
    private val mLlTab: LinearLayout

    private var onItemSelectedListener: OnItemSelectedListener? = null
    private var mOnPageChangeInterceptor: OnPageChangeInterceptor? = null

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarLayout)
        initAttrs(ta, context)
        mLlTab = LinearLayout(context)
        mLlTab.orientation = LinearLayout.HORIZONTAL
        if (barBackground != null) {
            mLlTab.background = barBackground
        } else {
            mLlTab.setBackgroundColor(UIUtils.getColor(context, R.color.tab_gb))
        }
        addView(mLlTab)
        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        APieLog.d(TAG, "width: $measuredWidth height: $barHeight")
        val params = LayoutParams(measuredWidth, barHeight)
        params.gravity = Gravity.BOTTOM
        mLlTab.layoutParams = params
    }

    private fun initAttrs(ta: TypedArray, context: Context) {
        mSmoothScroll = ta.getBoolean(R.styleable.BottomBarLayout_smoothScroll, mSmoothScroll)
        mSameTabClickCallBack =
            ta.getBoolean(R.styleable.BottomBarLayout_sameTabClickCallBack, mSameTabClickCallBack)
        barBackground = ta.getDrawable(R.styleable.BottomBarLayout_barBackground)
        barHeight = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_barHeight,
            UIUtils.dip2Px(context, barHeight)
        )
        floatEnable = ta.getBoolean(R.styleable.BottomBarLayout_floatEnable, floatEnable)
        floatIcon = ta.getDrawable(R.styleable.BottomBarLayout_floatIcon)
        floatMarginBottom = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_floatMarginBottom,
            UIUtils.dip2Px(context, floatMarginBottom)
        )
        floatIconWidth = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_floatIconWidth,
            UIUtils.dip2Px(context, floatIconWidth)
        )
        floatIconHeight = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_floatIconHeight,
            UIUtils.dip2Px(context, floatIconHeight)
        )


        titleTextBold = ta.getBoolean(R.styleable.BottomBarLayout_itemTextBold, titleTextBold)
        titleTextSize = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_itemTextSize,
            UIUtils.sp2px(context, titleTextSize)
        )

        titleNormalColor = ta.getColor(
            R.styleable.BottomBarLayout_textColorNormal,
            UIUtils.getColor(context, R.color.bbl_999999)
        )
        titleSelectedColor = ta.getColor(
            R.styleable.BottomBarLayout_textColorSelected,
            UIUtils.getColor(context, R.color.bbl_ff0000)
        )

        marginTop = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_itemMarginTop,
            UIUtils.dip2Px(context, marginTop)
        )

        iconWidth = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_iconWidth, 0)
        iconHeight = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_iconHeight, 0)
        itemPadding = ta.getDimensionPixelSize(R.styleable.BottomBarLayout_itemPadding, 0)

        unreadTextSize = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_unreadTextSize,
            UIUtils.sp2px(context, unreadTextSize)
        )
        unreadTextColor = ta.getColor(
            R.styleable.BottomBarLayout_unreadTextColor,
            UIUtils.getColor(context, R.color.apie_color_white)
        )
        unreadTextBg = ta.getDrawable(R.styleable.BottomBarLayout_unreadTextBg)

        msgTextSize = ta.getDimensionPixelSize(
            R.styleable.BottomBarLayout_msgTextSize,
            UIUtils.sp2px(context, msgTextSize)
        )
        msgTextColor = ta.getColor(
            R.styleable.BottomBarLayout_msgTextColor,
            UIUtils.getColor(context, R.color.apie_color_white)
        )
        msgTextBg = ta.getDrawable(R.styleable.BottomBarLayout_msgTextBg)

        notifyPointBg = ta.getDrawable(R.styleable.BottomBarLayout_notifyPointBg)

        unreadNumThreshold =
            ta.getInteger(R.styleable.BottomBarLayout_unreadThreshold, unreadNumThreshold)
    }

    fun setViewPager(viewPager2: ViewPager2?) {
        this.mViewPager2 = viewPager2

        mViewPager2?.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                handlePageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun createBottomBarItem(
        normalIcon: Drawable?,
        selectedIcon: Drawable?,
        title: String,
        iconWidth: Int,
        iconHeight: Int,
        lottieJson: String
    ): APieNavBarItem {
        return APieNavBarItem.Builder(context)
            .titleTextBold(titleTextBold)
            .titleTextSize(titleTextSize)
            .titleNormalColor(titleNormalColor)
            .iconHeight(iconHeight)
            .iconWidth(iconWidth)
            .marginTop(marginTop)
            .itemPadding(itemPadding)
            .titleSelectedColor(titleSelectedColor)
            .lottieJson(lottieJson)
            .unreadNumThreshold(unreadNumThreshold)
            .unreadTextBg(unreadTextBg)
            .unreadTextSize(unreadTextSize)
            .unreadTextColor(unreadTextColor)
            .msgTextBg(msgTextBg)
            .msgTextColor(msgTextColor)
            .msgTextSize(msgTextSize)
            .notifyPointBg(notifyPointBg)
            .build(normalIcon, selectedIcon, title)
    }

    fun setData(tabData: List<TabData>?) {
        require(!tabData.isNullOrEmpty()) { "tabData is null" }
        mItemViews.clear()
        mLlTab.removeAllViews()

        //添加tab
        for (i in tabData.indices) {
            val itemData = tabData[i]
            val normalIcon =
                if (!TextUtils.isEmpty(itemData.lottieJson)) {
                    null
                } else if (itemData.normalIcon != null) {
                    itemData.normalIcon
                } else {
                    ContextCompat.getDrawable(context, itemData.normalIconResId)
                }
            val selectedIcon =
                if (!TextUtils.isEmpty(itemData.lottieJson)) {
                    null
                } else if (itemData.selectedIcon != null){
                    itemData.selectedIcon
                } else {
                    ContextCompat.getDrawable(context, itemData.selectedIconResId)
                }
            val iconWidth = if (itemData.iconWidth == 0) this.iconWidth else itemData.iconWidth
            val iconHeight = if (itemData.iconHeight == 0) this.iconHeight else itemData.iconHeight
            val item = createBottomBarItem(
                normalIcon,
                selectedIcon,
                itemData.title,
                iconWidth,
                iconHeight,
                itemData.lottieJson
            )
            addItem(item)
        }

        // 如果开启凸起 且是 其他tab总数是偶数
        if (floatEnable && tabData.size % 2 == 0) {
            val item =
                createBottomBarItem(floatIcon, floatIcon, "", floatIconWidth, floatIconHeight, "")
            addItem(item, (tabData.size + 1) / 2, true)
        }

        mItemViews[0].refreshTab(true)
    }

    @JvmOverloads
    fun addItem(item: APieNavBarItem, index: Int = -1, isFloatItem: Boolean = false) {
        if (index == -1) {
            mItemViews.add(item)
        } else {
            mItemViews.add(index, item)
        }

        val position = if (index != -1) index else mItemViews.size - 1
        APieLog.d(TAG, "position: $position")

        var view: View = item
        val layoutParams = LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT)
        layoutParams.weight = 1f
        layoutParams.gravity = Gravity.CENTER
        view.layoutParams = layoutParams

        if (isFloatItem) {
            val params = LayoutParams(floatIconWidth, floatIconHeight)
            params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            params.bottomMargin = floatMarginBottom
            addView(item, params)
            view = View(context)
        }

        mLlTab.addView(view, position, layoutParams)

        //tab添加点击事件
        for (i in mItemViews.indices) {
            mItemViews[i].setOnClickListener(MyOnClickListener(i))
        }
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < mItemViews.size) {
            val item = mItemViews[position]
            if (mItemViews.contains(item)) {
                resetState()
                mLlTab.removeViewAt(position)
            }
            mItemViews.remove(item)

            //tab添加点击事件
            for (i in mItemViews.indices) {
                mItemViews[i].setOnClickListener(MyOnClickListener(i))
            }
        }
    }

    private fun handlePageSelected(position: Int) {
        //滑动时判断是否需要拦截跳转
        if (mOnPageChangeInterceptor != null
            && mOnPageChangeInterceptor!!.onIntercepted(position)
        ) {
            currentItem = mCurrentItem
            return
        }
        resetState()
        mItemViews[position].refreshTab(true)
        val prePos = mCurrentItem
        mCurrentItem = position //记录当前位置
        onItemSelectedListener?.onItemSelected(getBottomItem(mCurrentItem), prePos, mCurrentItem)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        handlePageSelected(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    private fun updateTabState(position: Int) {
        resetState()
        mCurrentItem = position
        mItemViews[mCurrentItem].refreshTab(true)
    }

    /**
     * 重置当前按钮的状态
     */
    private fun resetState() {
        if (mCurrentItem < mItemViews.size) {
            if (mItemViews[mCurrentItem].isSelected) {
                mItemViews[mCurrentItem].refreshTab(false)
            }
        }
    }

    /**
     * 设置未读数
     *
     * @param position  底部标签的下标
     * @param unreadNum 未读数
     */
    fun setUnread(position: Int, unreadNum: Int) {
        mItemViews[position].setUnreadNum(unreadNum)
    }

    /**
     * 设置提示消息
     *
     * @param position 底部标签的下标
     * @param msg      未读数
     */
    fun setMsg(position: Int, msg: String?) {
        mItemViews[position].setMsg(msg!!)
    }

    /**
     * 隐藏提示消息
     *
     * @param position 底部标签的下标
     */
    fun hideMsg(position: Int) {
        mItemViews[position].hideMsg()
    }

    /**
     * 显示提示的小红点
     *
     * @param position 底部标签的下标
     */
    fun showNotify(position: Int) {
        mItemViews[position].showNotify()
    }

    /**
     * 隐藏提示的小红点
     *
     * @param position 底部标签的下标
     */
    fun hideNotify(position: Int) {
        mItemViews[position].hideNotify()
    }

    var currentItem: Int
        get() = mCurrentItem
        set(currentItem) {
            mViewPager2?.setCurrentItem(currentItem, mSmoothScroll) ?: {
                onItemSelectedListener?.onItemSelected(
                    getBottomItem(currentItem),
                    mCurrentItem,
                    currentItem
                )
            }
        }

    /**
     * 设置是否开启滑动
     */
    fun setSmoothScroll(smoothScroll: Boolean) {
        this.mSmoothScroll = smoothScroll
    }

    /**
     * 获取底部导航栏的item
     */
    fun getBottomItem(position: Int): APieNavBarItem {
        return mItemViews[position]
    }

    /**
     * 设置底部导航栏选中监听
     */
    fun setOnItemSelectedListener(onItemSelectedListener: OnItemSelectedListener?) {
        this.onItemSelectedListener = onItemSelectedListener
    }

    /**
     * 滑动拦截
     */
    fun setOnPageChangeInterceptor(onPageChangedInterceptor: OnPageChangeInterceptor?) {
        this.mOnPageChangeInterceptor = onPageChangedInterceptor
    }

    /**
     * 同一个item是否支持多次点击
     */
    fun setSameTabClickCallBack(sameTabClickCallBack: Boolean) {
        this.mSameTabClickCallBack = sameTabClickCallBack
    }

    private inner class MyOnClickListener(private val currentIndex: Int) : OnClickListener {
        override fun onClick(v: View) {
            // 点击时判断是否需要拦截跳转
            if (mOnPageChangeInterceptor != null && mOnPageChangeInterceptor!!.onIntercepted(
                    currentIndex
                )
            ) {
                return
            }
            if (currentIndex == mCurrentItem) {
                //如果还是同个页签，判断是否要回调
                onItemSelectedListener?.let {
                    if (mSameTabClickCallBack) {
                        it.onItemSameTabClick(getBottomItem(currentIndex), mCurrentItem)
                    }
                }
            } else {
                mViewPager2?.let {
                    it.setCurrentItem(currentIndex, mSmoothScroll)
                    return
                }
                onItemSelectedListener?.onItemSelected(
                    getBottomItem(currentIndex),
                    mCurrentItem,
                    currentIndex
                )
                updateTabState(currentIndex)
            }
        }
    }

    interface OnPageChangeInterceptor {
        fun onIntercepted(position: Int): Boolean
    }

    interface OnItemSelectedListener {
        fun onItemSelected(
            aPieNavBarItem: APieNavBarItem?,
            previousPosition: Int,
            currentPosition: Int
        )

        fun onItemSameTabClick(
            aPieNavBarItem: APieNavBarItem?,
            currentPosition: Int
        )
    }
}
