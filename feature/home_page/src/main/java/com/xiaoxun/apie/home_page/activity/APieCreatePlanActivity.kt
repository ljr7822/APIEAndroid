package com.xiaoxun.apie.home_page.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.xiaoxun.apie.common.HOME_CREATE_PLAN_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.R
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreatePlanActivityBinding

@Route(path = HOME_CREATE_PLAN_ACTIVITY_PATH)
class APieCreatePlanActivity : APieBaseBindingActivity<LayoutApieCreatePlanActivityBinding>(
    LayoutApieCreatePlanActivityBinding::inflate
) {

    override fun initializeView() {
        super.initializeView()
        binding.topBar.leftIconView.setDebouncingClickListener {
            finish()
        }
    }
}