package com.example.xiaoxun.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.marginTop
import com.example.xiaoxun.databinding.LayoutHomeFragmentBinding
import com.xiaoxun.apie.common.base.fragment.APieBaseTabFragment
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.StatusBarUtils
import com.xiaoxun.apie.common.utils.setMargin
import com.xiaoxun.apie.common.utils.setMarginEnd
import com.xiaoxun.apie.feature.account.AccountLoginViewModel

class HomeFragment: APieBaseTabFragment<LayoutHomeFragmentBinding>(LayoutHomeFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    private val viewModel: AccountLoginViewModel by lazy { AccountLoginViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startBtn.setOnClickListener {
            viewModel.login("test", "test", "111111")
        }
    }
}