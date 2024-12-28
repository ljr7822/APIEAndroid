package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.xiaoxun.apie.common.HOME_SETTING_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexDesireFragmentBinding

/**
 * 心愿Fragment
 */
class APieIndexDesireFragment : APieBaseBindingFragment<LayoutApieIndexDesireFragmentBinding>(
    LayoutApieIndexDesireFragmentBinding::inflate
) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initTopBarView() {
        super.initTopBarView()
        binding.topBar.leftIconView.setDebouncingClickListener {
            ARouter.getInstance().build(HOME_SETTING_ACTIVITY_PATH).navigation()
        }
    }

}