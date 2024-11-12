package com.xiaoxun.apie.common.navbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import com.airbnb.lottie.LottieAnimationView
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.UIUtils
import java.util.Locale


class BottomBarItem @JvmOverloads constructor(
    private val context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mImageView: ImageView? = null
    private var mLottieView: LottieAnimationView? = null
    private var mTvUnread: TextView? = null
    private var mTvNotify: TextView? = null
    private var mTvMsg: TextView? = null
    private var mTextView: TextView? = null

    private var mBuilder: Builder? = null

    private fun checkValues() {
        mBuilder?.apply {
            if (unreadTextBg == null) {
                unreadTextBg = context.getDrawable(R.drawable.shape_unread)
            }
            if (msgTextBg == null) {
                msgTextBg = context.getDrawable(R.drawable.shape_msg)
            }
            if (notifyPointBg == null) {
                notifyPointBg = context.getDrawable(R.drawable.shape_notify_point)
            }
        } ?: throw IllegalStateException("Builder is null")
    }

    private fun init() {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        val view = initView()
        val params = mImageView?.layoutParams as FrameLayout.LayoutParams

        mBuilder?.apply {
            if (iconWidth != 0 && iconHeight != 0) {
                params.width = iconWidth
                params.height = iconHeight
            }

            if (!lottieJson.isNullOrEmpty()) {
                mLottieView?.apply {
                    layoutParams = params
                    setAnimation(lottieJson)
                    repeatCount = 0
                }
            } else {
                mImageView?.apply {
                    setImageDrawable(normalIcon)
                    this.layoutParams = params
                }
            }

            mTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())
            mTextView?.paint?.isFakeBoldText = titleTextBold
            mTvUnread?.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, unreadTextSize.toFloat())
                setTextColor(unreadTextColor)
                background = unreadTextBg
            }

            mTvMsg?.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, msgTextSize.toFloat())
                setTextColor(msgTextColor)
                background = msgTextBg
            }

            mTvNotify?.background = notifyPointBg
            mTextView?.setTextColor(titleNormalColor)
            mTextView?.text = title

            (mTextView?.layoutParams as LayoutParams).topMargin = marginTop
        }

        addView(view)
    }

    @NonNull
    private fun initView(): View {
        val view = View.inflate(context, R.layout.item_bottom_bar, null)
        mBuilder?.apply {
            if (itemPadding != 0) {
                view.setPadding(itemPadding, itemPadding, itemPadding, itemPadding)
            }
        }
        mImageView = view.findViewById(R.id.iv_icon)
        mLottieView = view.findViewById(R.id.lottieView)
        mTvUnread = view.findViewById(R.id.tv_unred_num)
        mTvMsg = view.findViewById(R.id.tv_msg)
        mTvNotify = view.findViewById(R.id.tv_point)
        mTextView = view.findViewById(R.id.tv_text)

        mBuilder?.let {
            mImageView?.visibility =
                if (!TextUtils.isEmpty(it.lottieJson)) View.GONE else View.VISIBLE
            mLottieView?.visibility =
                if (!TextUtils.isEmpty(it.lottieJson)) View.VISIBLE else View.GONE
        }

        return view
    }

    val title: String?
        get() = mBuilder?.title

    fun getImageView(): ImageView? = mImageView
    fun getTextView(): TextView? = mTextView

    fun setNormalIcon(normalIcon: Drawable) {
        mBuilder?.normalIcon = normalIcon
        refreshTab()
    }

    fun setSelectedIcon(selectedIcon: Drawable) {
        mBuilder?.selectedIcon = selectedIcon
        refreshTab()
    }

    fun refreshTab(isSelected: Boolean = false) {
        this.isSelected = isSelected
        refreshTab()
    }

    private fun refreshTab() {
        mBuilder?.let {
            if (!TextUtils.isEmpty(it.lottieJson)) {
                if (isSelected) {
                    mLottieView?.playAnimation()
                } else {
                    mLottieView?.cancelAnimation()
                    mLottieView?.progress = 0f
                }
            } else {
                mImageView?.setImageDrawable(if (isSelected) it.selectedIcon else it.normalIcon)
            }
            mTextView?.setTextColor(if (isSelected) it.titleSelectedColor else it.titleNormalColor)
        }
    }

    fun setUnreadNum(unreadNum: Int) {
        setTvVisible(mTvUnread)
        mTvUnread?.apply {
            visibility = if (unreadNum <= 0) View.GONE else View.VISIBLE
            text = if (unreadNum <= mBuilder?.unreadNumThreshold ?: 0) unreadNum.toString()
            else String.format(Locale.CHINA, "%d+", mBuilder?.unreadNumThreshold)
        }
    }

    fun setMsg(msg: String) {
        setTvVisible(mTvMsg)
        mTvMsg?.text = msg
    }

    fun hideMsg() {
        mTvMsg?.visibility = View.GONE
    }

    fun showNotify() {
        setTvVisible(mTvNotify)
    }

    fun hideNotify() {
        mTvNotify?.visibility = View.GONE
    }

    private fun setTvVisible(tv: TextView?) {
        mTvUnread?.visibility = View.GONE
        mTvMsg?.visibility = View.GONE
        mTvNotify?.visibility = View.GONE
        tv?.visibility = View.VISIBLE
    }

    fun create(builder: Builder): BottomBarItem {
        mBuilder = builder
        checkValues()
        init()
        return this
    }

    class Builder(private val context: Context) {
        var normalIcon: Drawable? = null // 普通状态图标的资源id
        var selectedIcon: Drawable? = null // 选中状态图标的资源id
        var title: String? = null // 标题
        var titleTextBold: Boolean = false // 文字加粗
        var titleTextSize: Int = UIUtils.sp2px(context, 12) // 字体大小
        var titleNormalColor: Int = getColor(R.color.bbl_999999) // 描述文本的默认显示颜色
        var titleSelectedColor: Int = getColor(R.color.bbl_ff0000) // 描述文本的默认选中显示颜色
        var marginTop: Int = 0 // 文字和图标的距离
        var iconWidth: Int = 0 // 图标的宽度
        var iconHeight: Int = 0 // 图标的高度
        var itemPadding: Int = 0 // BottomBarItem的padding
        var unreadTextSize: Int = UIUtils.sp2px(context, 10) // 未读数字体大小
        var unreadNumThreshold: Int = 99 // 未读数阈值
        var unreadTextColor: Int = getColor(R.color.white) // 未读数字体颜色
        var unreadTextBg: Drawable? = null // 未读数文字背景
        var msgTextSize: Int = UIUtils.sp2px(context, 6) // 消息字体大小
        var msgTextColor: Int = getColor(R.color.white) // 消息文字颜色
        var msgTextBg: Drawable? = null // 消息提醒背景颜色
        var notifyPointBg: Drawable? = null // 小红点背景颜色
        var lottieJson: String? = null // lottie文件名

        /**
         * Sets the default icon's resourceId
         */
        fun normalIcon(normalIcon: Drawable) = apply { this.normalIcon = normalIcon }

        /**
         * Sets the selected icon's resourceId
         */
        fun selectedIcon(selectedIcon: Drawable) = apply { this.selectedIcon = selectedIcon }

        /**
         * Sets the title's resourceId
         */
        fun title(titleId: Int) = apply { this.title = context.getString(titleId) }

        /**
         * Sets the title string
         */
        fun title(title: String) = apply { this.title = title }

        /**
         * Sets the title's text bold
         */
        fun titleTextBold(titleTextBold: Boolean) = apply { this.titleTextBold = titleTextBold }

        /**
         * Sets the title's text size
         */
        fun titleTextSize(titleTextSize: Int) = apply { this.titleTextSize = titleTextSize }

        /**
         * Sets the title's normal color resourceId
         */
        fun titleNormalColor(titleNormalColor: Int) =
            apply { this.titleNormalColor = titleNormalColor }

        /**
         * Sets the title's selected color resourceId
         */
        fun titleSelectedColor(titleSelectedColor: Int) =
            apply { this.titleSelectedColor = titleSelectedColor }

        /**
         * Sets the item's margin top
         */
        fun marginTop(marginTop: Int) = apply { this.marginTop = marginTop }

        /**
         * Sets icon's width
         */
        fun iconWidth(iconWidth: Int) = apply { this.iconWidth = iconWidth }

        /**
         * Sets icon's height
         */
        fun iconHeight(iconHeight: Int) = apply { this.iconHeight = iconHeight }

        /**
         * Sets padding for item
         */
        fun itemPadding(itemPadding: Int) = apply { this.itemPadding = itemPadding }

        /**
         * Sets unread font size
         */
        fun unreadTextSize(unreadTextSize: Int) = apply { this.unreadTextSize = unreadTextSize }

        /**
         * Sets the number of unread array thresholds greater than the threshold to be displayed as n + n as the set threshold
         */
        fun unreadNumThreshold(unreadNumThreshold: Int) =
            apply { this.unreadNumThreshold = unreadNumThreshold }

        /**
         * Sets the message font size
         */
        fun msgTextSize(msgTextSize: Int) = apply { this.msgTextSize = msgTextSize }

        /**
         * Sets the message font background
         */
        fun unreadTextBg(unreadTextBg: Drawable?) = apply { this.unreadTextBg = unreadTextBg }

        /**
         * Sets unread font color
         */
        fun unreadTextColor(unreadTextColor: Int) = apply { this.unreadTextColor = unreadTextColor }

        /**
         * Sets the message font color
         */
        fun msgTextColor(msgTextColor: Int) = apply { this.msgTextColor = msgTextColor }

        /**
         * Sets the message font background
         */
        fun msgTextBg(msgTextBg: Drawable?) = apply { this.msgTextBg = msgTextBg }

        /**
         * Set the message prompt point background
         */
        fun notifyPointBg(notifyPointBg: Drawable?) = apply { this.notifyPointBg = notifyPointBg }

        /**
         * Set the name of lottie json file
         */
        fun lottieJson(lottieJson: String) = apply { this.lottieJson = lottieJson }

        /**
         * Create a BottomBarItem object
         */
        fun build(normalIcon: Drawable?, selectedIcon: Drawable?, text: String): BottomBarItem {
            this.normalIcon = normalIcon
            this.selectedIcon = selectedIcon
            title = text

            val bottomBarItem = BottomBarItem(context)
            return bottomBarItem.create(this)
        }

        fun build(normalIconId: Int, selectedIconId: Int, text: String): BottomBarItem {
            return build(
                UIUtils.getDrawable(context, normalIconId),
                UIUtils.getDrawable(context, selectedIconId),
                text
            )
        }

        private fun getColor(colorId: Int): Int {
            return context.resources.getColor(colorId)
        }
    }

}