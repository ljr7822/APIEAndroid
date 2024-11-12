package com.example.xiaoxun

import androidx.viewbinding.ViewBinding
import com.example.xiaoxun.databinding.LayoutApieHomeActivityBinding
import com.example.xiaoxun.nav.APieViewPagerActivity


class APieHomeActivity : APieViewPagerActivity() {

    override fun getViewBinding(): ViewBinding {
        return LayoutApieHomeActivityBinding.inflate(layoutInflater)
    }
}