package com.example.xiaoxun

import android.os.Bundle
import com.example.xiaoxun.databinding.LayoutApieWelcomeActivityBinding
import com.gyf.immersionbar.ImmersionBar
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity

/**
 * 欢迎页
 */
class APieWelcomeActivity : APieBaseBindingActivity<LayoutApieWelcomeActivityBinding>(
    LayoutApieWelcomeActivityBinding::inflate
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowsStyle()
    }

    private fun initWindowsStyle() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
    }
}