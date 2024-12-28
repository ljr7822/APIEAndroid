package com.xiaoxun.apie.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.xiaoxun.apie.common.R

class APieExpandBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val goldLayout: LinearLayout
    val goldValue: TextView

    init {
        orientation = HORIZONTAL
        LayoutInflater.from(context).inflate(R.layout.apie_expand_bar, this, true)

        goldLayout = findViewById(R.id.goldLayout)
        goldValue = findViewById(R.id.goldValue)
    }

    fun setGoldValue(value: String) {
        goldValue.text = value
    }
}