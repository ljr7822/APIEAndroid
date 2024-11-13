package com.example.xiaoxun.fragment

import android.os.Bundle
import android.view.View
import com.example.xiaoxun.databinding.LayoutDesireFragmentBinding
import com.xiaoxun.apie.common.base.fragment.APieBaseTabFragment

/**
 * 心愿Fragment
 */
class DesireFragment: APieBaseTabFragment<LayoutDesireFragmentBinding>(LayoutDesireFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testFlag.text = requireArguments().getString(TEST_FLAG)
    }
}