package com.example.xiaoxun.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.xiaoxun.databinding.LayoutHomeFragmentBinding
import com.xiaoxun.apie.account.viewmodel.AccountViewModel
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.utils.toast.APieToast
import kotlinx.coroutines.launch

class HomeFragment: APieBaseBindingFragment<LayoutHomeFragmentBinding>(LayoutHomeFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    private val viewModel: AccountViewModel by lazy { AccountViewModel() }

    private var phoneNum: String = "18289816889"
    private var userId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startPassword.setOnClickListener {
//            viewLifecycleOwner.lifecycleScope.launch {
//                viewModel.loginByPassword(phoneNum, "123456").also { res ->
//                    res.getOrNull()?.data?.let { data ->
//                        phoneNum = data.phoneNum
//                        userId = data.userId
//                        binding.tv.text = res.toString()
//                    }
//                }
//            }
            APieToast.showDialog("这是一个toast")
        }

//        binding.startCode.setOnClickListener {
//            viewLifecycleOwner.lifecycleScope.launch {
//                viewModel.loginBySmsCode(phoneNum, getCode()).also { res ->
//                    res.getOrNull()?.data?.let { data ->
//                        phoneNum = data.phoneNum
//                        userId = data.userId
//                        binding.tv.text = res.toString()
//                    }
//                }
//            }
//        }
//
//        binding.getCode.setOnClickListener {
//            viewLifecycleOwner.lifecycleScope.launch {
//                viewModel.sendSmsCode(phoneNum, userId).also {res ->
//                    res.getOrNull()?.data?.let { data ->
//                        binding.tv.text = res.toString()
//                    }
//                }
//            }
//        }

    }

    private fun getCode(): String {
        return binding.etCode.text.toString()
    }
}