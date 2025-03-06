package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.lxj.xpopup.XPopup
import com.xiaoxun.apie.common.APP_WELCOME_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadImageWithTransformation
import com.xiaoxun.apie.common.ui.easy_glide.transformation.CircleWithBorderTransformation
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.repo.DesireMMKVRepository
import com.xiaoxun.apie.common.repo.PlanMMKVRepository
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.ui.APieStyleExitTip
import com.xiaoxun.apie.common.ui.APieStyleExitTip.Companion.APIE_CACHE_APP_CACHE_KEY
import com.xiaoxun.apie.common.ui.APieStyleExitTip.Companion.APIE_LOGIN_OUT_APP_KEY
import com.xiaoxun.apie.common.utils.ThreadUtil
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.mine.MineSettingInfo
import com.xiaoxun.apie.common_model.home_page.mine.MineSettingItemAction
import com.xiaoxun.apie.home_page.adapter.mine.APieMineSettingAdapter
import com.xiaoxun.apie.home_page.adapter.mine.SettingRepo
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexMineFragmentBinding
import com.xiaoxun.apie.home_page.fragment.setting.APieAccountFragment
import com.xiaoxun.apie.home_page.fragment.setting.APieEditProfileFragment
import com.xiaoxun.apie.home_page.fragment.setting.APieGroupManagerFragment
import com.xiaoxun.apie.home_page.fragment.setting.APieSoundEffectsFragment
import com.xiaoxun.apie.home_page.repo.mine.MineRepo
import com.xiaoxun.apie.home_page.viewmodel.IndexMineViewModel
import com.xiaoxun.apie.home_page.widget.APieLeftDrawerPopupView

/**
 * 我的Fragment
 */
class APieIndexMineFragment :
    APieBaseBindingFragment<LayoutApieIndexMineFragmentBinding>(LayoutApieIndexMineFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    private val loadingDialog: APieLoadingDialog by lazy { APieLoadingDialog(hostActivity) }

    private val mineSettingAdapter: APieMineSettingAdapter by lazy { APieMineSettingAdapter() }

    private val viewModel: IndexMineViewModel by lazy { IndexMineViewModel() }

    private val repo: MineRepo by lazy { MineRepo(hostActivity, viewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initTopBarMenu()
        initAvatarView()
        initUserInfo()
        initUserDataInfo()
        initRecyclerView()
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

        if (AccountMMKVRepository.sex == 1) {
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

    private fun initRecyclerView() {
        mineSettingAdapter.replayData(SettingRepo.buildSettingList())
        binding.mineSettingRecyclerView.apply {
            adapter = mineSettingAdapter
            layoutManager = LinearLayoutManager(context)
        }

        mineSettingAdapter.setOnItemClickListener(object :
            APieMineSettingAdapter.OnItemClickListener {
            override fun itemClick(position: Int, item: MineSettingInfo) {
                onItemClick(position, item)
            }
        })
    }

    private fun onItemClick(position: Int, item: MineSettingInfo) {
        when (item.mineSettingItemAction) {
            MineSettingItemAction.ACTION_EDIT_PROFILE -> {
                // 编辑资料
                (context as? FragmentActivity)?.supportFragmentManager?.let {
                    APieEditProfileFragment(viewModel, repo).show(
                        it,
                        APieEditProfileFragment::class.java.simpleName
                    )
                }
            }

            MineSettingItemAction.ACTION_ACCOUNT_SETTING -> {
                // 账号设置
                (context as? FragmentActivity)?.supportFragmentManager?.let {
                    APieAccountFragment(viewModel, repo).show(
                        it,
                        APieAccountFragment::class.java.simpleName
                    )
                }
            }

            MineSettingItemAction.ACTION_NOTIFY_SETTING -> {
                // 通知设置
            }

            MineSettingItemAction.ACTION_DATA_ANALYSIS -> {
                // 数据分析
            }

            MineSettingItemAction.ACTION_SOUND_EFFECTS -> {
                // 音效设置
                (context as? FragmentActivity)?.supportFragmentManager?.let {
                    APieSoundEffectsFragment().show(
                        it,
                        APieSoundEffectsFragment::class.java.simpleName
                    )
                }
            }

            MineSettingItemAction.ACTION_GROUP_MANAGER -> {
                // 分组管理
                (context as? FragmentActivity)?.supportFragmentManager?.let {
                    APieGroupManagerFragment().show(
                        it,
                        APieGroupManagerFragment::class.java.simpleName
                    )
                }
            }

            MineSettingItemAction.ACTION_LIST_STYLE -> {
                // 列表风格
            }

            MineSettingItemAction.ACTION_STORAGE -> {
                // 存储
                logout(APIE_CACHE_APP_CACHE_KEY)
            }

            MineSettingItemAction.ACTION_ABOUT -> {
                // 关于
            }

            MineSettingItemAction.ACTION_LOGOUT -> {
                // 退出登录
                logout(APIE_LOGIN_OUT_APP_KEY)
            }
        }
    }

    private fun logout(key: String) {
        APieStyleExitTip.show(
            activity = hostActivity,
            key = key,
            actionCallback = {
                loadingDialog.show()
                AccountManager.logout()
                ThreadUtil.runOnMainThreadDelay({
                    loadingDialog.dismiss()
                    hostActivity.finish()
                    ARouter.getInstance().build(APP_WELCOME_ACTIVITY_PATH).navigation()
                }, 500)
            }
        )
    }
}