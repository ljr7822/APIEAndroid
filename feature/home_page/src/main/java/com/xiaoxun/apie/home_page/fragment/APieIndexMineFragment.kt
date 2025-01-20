package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import com.lxj.xpopup.XPopup
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadImageWithTransformation
import com.xiaoxun.apie.common.ui.easy_glide.transformation.CircleWithBorderTransformation
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.repo.DesireMMKVRepository
import com.xiaoxun.apie.common.repo.PlanMMKVRepository
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexMineFragmentBinding
import com.xiaoxun.apie.home_page.widget.APieLeftDrawerPopupView

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
        initView()
    }

    private fun initView() {
        initTopBarMenu()
        initAvatarView()
        initUserInfo()
        initUserDataInfo()
    }

    private fun initTopBarMenu() {
        binding.mineTopMenuIcon.setDebouncingClickListener {
            showLeftDrawerPopupView()
        }
    }

    private fun initAvatarView() {
        binding.avatarImageView.loadImageWithTransformation(
            hostActivity,
            AccountMMKVRepository.userAvatar,
            CircleWithBorderTransformation(0, 0)
        )

        if (AccountMMKVRepository.sex == 0) {
            binding.sexIcon.setImageResource(R.drawable.apie_mine_male_icon)
        } else {
            binding.sexIcon.setImageResource(R.drawable.apie_mine_female_icon)
        }
    }

    private fun initUserInfo() {
        binding.nickname.text = AccountMMKVRepository.userName
        binding.apieId.text = AccountMMKVRepository.userId
        binding.locationView.text = AccountMMKVRepository.address
        binding.schoolView.text = AccountMMKVRepository.school
        binding.descView.text = AccountMMKVRepository.userDesc
    }

    private fun initUserDataInfo() {
        binding.planCount.text = PlanMMKVRepository.planCount.toString()
        binding.desireCount.text = DesireMMKVRepository.desireCount.toString()
        binding.goldCount.text = AccountMMKVRepository.goldCount.toString()
    }

    private fun showLeftDrawerPopupView() {
        XPopup.Builder(hostActivity)
            .isDestroyOnDismiss(true)
            .isViewMode(true)
            .asCustom(APieLeftDrawerPopupView(hostActivity))
            .show()
    }
}