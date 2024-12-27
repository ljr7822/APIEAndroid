package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexMineFragmentBinding

/**
 * 我的Fragment
 */
class APieIndexMineFragment :
    APieBaseBindingFragment<LayoutApieIndexMineFragmentBinding>(LayoutApieIndexMineFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testFlag.text = requireArguments().getString(TEST_FLAG)
    }
}