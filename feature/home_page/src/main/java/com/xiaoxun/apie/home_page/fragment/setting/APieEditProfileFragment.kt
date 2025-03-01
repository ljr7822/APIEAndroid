package com.xiaoxun.apie.home_page.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadCircleImage
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.home_page.databinding.LayoutApieEditProfileSettingFragmentBinding
import com.xiaoxun.apie.home_page.repo.mine.MineRepo
import com.xiaoxun.apie.common_model.view_model.CommonLoadingState
import com.xiaoxun.apie.home_page.viewmodel.IndexMineViewModel
import kotlinx.coroutines.launch

/**
 * 编辑资料的Fragment
 */
class APieEditProfileFragment(
    private val viewModel: IndexMineViewModel,
    private val repo: MineRepo
) :
    APieBaseBottomSheetDialogFragment<LayoutApieEditProfileSettingFragmentBinding>(
        LayoutApieEditProfileSettingFragmentBinding::inflate
    ) {

    private val loadingDialog: APieLoadingDialog by lazy {
        APieLoadingDialog(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableCancel = true
        super.onCreate(savedInstanceState)
        val screenHeight = UIUtils.getScreenRealHeight(requireContext())
        val viewH = screenHeight / 3 * 2
        peekHeight = viewH
        layoutHeight = viewH
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initData()
        initView()
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.loadUserInfo()
        }
    }

    private fun initView() {
        binding.userAvatarView.setDebouncingClickListener {
            // TODO xiaoxun 选择头像
            APieToast.showDialog("选择头像")
        }

        binding.editNickName.setDebouncingClickListener {
            // TODO xiaoxun 编辑昵称
            APieToast.showDialog("编辑昵称")
        }

        binding.editDesc.setDebouncingClickListener {
            // TODO xiaoxun 编辑描述
            APieToast.showDialog("编辑描述")
        }

        binding.editSexIcon.setDebouncingClickListener {
            // TODO xiaoxun 编辑性别
            APieToast.showDialog("编辑性别")
        }

        binding.editAddressIcon.setDebouncingClickListener {
            // TODO xiaoxun 编辑地址
            APieToast.showDialog("编辑地址")
        }

        binding.editSchoolIcon.setDebouncingClickListener {
            // TODO xiaoxun 编辑学校
            APieToast.showDialog("编辑学校")
        }
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

        viewModel.userInfo.observe(viewLifecycleOwner) {
            binding.userAvatarView.loadCircleImage(requireContext(), it.userPortrait)
            binding.nickNameView.text = it.userName
            binding.descView.text = it.desc
            binding.sexView.text = if (it.sex == 1) "男" else "女"
            binding.addressView.text = it.address
            binding.schoolView.text = it.school
        }
    }
}