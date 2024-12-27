package com.example.xiaoxun

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.xiaoxun.databinding.LayoutXxTestActivityBinding
import com.xiaoxun.apie.common.APP_XX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity

@Route(path = APP_XX_ACTIVITY_PATH)
class XxTestActivity : APieBaseBindingActivity<LayoutXxTestActivityBinding>(
    LayoutXxTestActivityBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}