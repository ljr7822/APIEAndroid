package com.example.xiaoxun.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.xiaoxun.databinding.LayoutHomeFragmentBinding
import com.xiaoxun.apie.account.vm.AccountViewModel
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import kotlinx.coroutines.launch

class HomeFragment: APieBaseBindingFragment<LayoutHomeFragmentBinding>(LayoutHomeFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    private val viewModel: AccountViewModel by lazy { AccountViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startBtn.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.login("test", "test", "111111").also {
                    it.getOrNull()?.message?.let { message ->
                        binding.tv.text = message
                    }
                }
            }
        }
    }
}