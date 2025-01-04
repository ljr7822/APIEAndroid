package com.xiaoxun.apie.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.xiaoxun.apie.common.R

class APieTopBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val rootViewLayout: ConstraintLayout
    val titleTextView: TextView
    val leftIconView: ImageView
    val rightIconView: ImageView
    val avatarImageView: GlideCircleImageView

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.apie_top_bar, this, true)

        rootViewLayout = findViewById(R.id.rootViewLayout)
        titleTextView = findViewById(R.id.titleTextView)
        leftIconView = findViewById(R.id.leftIconView)
        rightIconView = findViewById(R.id.rightIconView)
        avatarImageView = findViewById(R.id.avatarImageView)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.APieTopBarView)
        rootViewLayout.background = attributes.getDrawable(R.styleable.APieTopBarView_rootViewBg)
        titleTextView.text = attributes.getString(R.styleable.APieTopBarView_titleText) ?: context.getString(R.string.app_name)
        leftIconView.setImageResource(attributes.getResourceId(R.styleable.APieTopBarView_leftIconRes, R.drawable.apie_top_bar_menu_icon))
        leftIconView.visibility = if (attributes.getBoolean(R.styleable.APieTopBarView_leftIconEnable, true)) VISIBLE else GONE
        rightIconView.setImageResource(attributes.getResourceId(R.styleable.APieTopBarView_rightIconRes, R.drawable.apie_top_bar_filter_icon))
        rightIconView.visibility = if (attributes.getBoolean(R.styleable.APieTopBarView_rightIconEnable, true)) VISIBLE else GONE
        attributes.recycle()
    }

    fun setLeftIconEnable(enable: Boolean) {
        leftIconView.visibility = if (enable) VISIBLE else GONE
    }

    fun setRightIconEnable(enable: Boolean) {
        rightIconView.visibility = if (enable) VISIBLE else GONE
    }

    fun setAvatarViewEnable(enable: Boolean) {
        avatarImageView.visibility = if (enable) VISIBLE else GONE
    }

    fun setTitle(text: String) {
        titleTextView.text = text
    }

    fun setLeftIcon(resourceId: Int) {
        leftIconView.setImageResource(resourceId)
    }

    fun setRightIcon(resourceId: Int) {
        rightIconView.setImageResource(resourceId)
    }

    fun setAvatar(url: String) {
        avatarImageView.loadImage(url)
    }

    fun setAvatar(resourceId: Int) {
        avatarImageView.loadImage(resourceId)
    }
}