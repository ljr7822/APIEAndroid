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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}