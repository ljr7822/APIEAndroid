package com.example.xiaoxun

import android.os.Bundle
import com.example.xiaoxun.databinding.LayoutXxTestActivityBinding
import com.xiaoxun.apie.common.base.activity.APieBaseViewPagerActivity

class XxTestActivity : APieBaseViewPagerActivity<LayoutXxTestActivityBinding>(
    LayoutXxTestActivityBinding::inflate,
    true
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}