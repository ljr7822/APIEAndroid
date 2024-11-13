package com.example.xiaoxun.fragment

import android.os.Bundle
import android.view.View
import com.example.xiaoxun.databinding.LayoutHomeFragmentBinding
import com.xiaoxun.apie.common.base.fragment.APieBaseTabFragment

class HomeFragment: APieBaseTabFragment<LayoutHomeFragmentBinding>(LayoutHomeFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testFlag.text = requireArguments().getString(TEST_FLAG)
    }
}