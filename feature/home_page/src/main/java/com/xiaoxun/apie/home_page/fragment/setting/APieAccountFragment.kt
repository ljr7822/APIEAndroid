package com.xiaoxun.apie.home_page.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.view_model.CommonLoadingState
import com.xiaoxun.apie.home_page.bean.ChangePasswordInfo
import com.xiaoxun.apie.home_page.databinding.LayoutApieAccountSettingFragmentBinding
import com.xiaoxun.apie.home_page.dialog.APieChangePasswordDialog
import com.xiaoxun.apie.home_page.repo.mine.IMineRepo
import com.xiaoxun.apie.home_page.viewmodel.IndexMineViewModel
import kotlinx.coroutines.launch

class APieAccountFragment(
    private val viewModel: IndexMineViewModel,
    private val repo: IMineRepo,
) :
    APieBaseBottomSheetDialogFragment<LayoutApieAccountSettingFragmentBinding>(
        LayoutApieAccountSettingFragmentBinding::inflate
    ) {

    private val loadingDialog: APieLoadingDialog by lazy {
        APieLoadingDialog(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableCancel = true
        super.onCreate(savedInstanceState)
        val screenHeight = UIUtils.getScreenRealHeight(requireContext())
        val viewH = screenHeight / 2
        peekHeight = viewH
        layoutHeight = viewH
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initData()
        initView()
    }

    private fun initObserver() {
        viewModel.commonLoadingState.observe(viewLifecycleOwner) {
            when (it) {
                CommonLoadingState.START -> {
                    loadingDialog.show()
                }
                CommonLoadingState.SUCCESS -> {
                    loadingDialog.dismiss()
                }
                CommonLoadingState.FAILED -> {
                    loadingDialog.dismiss()
                }
                else -> {}
            }
        }
    }

    private fun initData() {
        binding.phoneContentNum.text = AccountMMKVRepository.phoneNum
        binding.pieIdContentView.text = AccountMMKVRepository.userId
    }

    private fun initView() {
        binding.phoneNumLayout.setDebouncingClickListener {
            APieToast.showDialog("暂不支持更换绑定手机")
        }

        binding.pieIdLayout.setDebouncingClickListener {
            APieToast.showDialog("苹果派ID已复制")
        }

        binding.passwordEditView.setDebouncingClickListener {
            APieChangePasswordDialog(
                context = requireContext(),
                onConfirm = {
                    changePassword(it)
                }).show()
        }

        binding.logOutLayout.setDebouncingClickListener {
            APieToast.showDialog("注销账号")
        }
    }

    private fun changePassword(changePasswordInfo: ChangePasswordInfo) {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.changePassword(changePasswordInfo)
        }
    }
}