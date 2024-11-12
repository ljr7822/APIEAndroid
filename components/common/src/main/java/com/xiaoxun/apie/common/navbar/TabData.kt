package com.xiaoxun.apie.common.navbar

import android.graphics.drawable.Drawable

data class TabData(
    var title: String,
    var normalIcon: Drawable? = null,
    var selectedIcon: Drawable? = null,
    var normalIconResId: Int = 0,
    var selectedIconResId: Int = 0,
    var lottieJson: String = "",
    var iconWidth: Int = 0,
    var iconHeight: Int = 0
) {
    constructor(title: String, normalIcon: Drawable, selectedIcon: Drawable) : this(title) {
        this.normalIcon = normalIcon
        this.selectedIcon = selectedIcon
    }

    constructor(title: String, normalIconResId: Int, selectedIconResId: Int) : this(title) {
        this.normalIconResId = normalIconResId
        this.selectedIconResId = selectedIconResId
    }

    constructor(title: String, lottieJson: String) : this(title) {
        this.lottieJson = lottieJson
    }
}



