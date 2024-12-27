package com.example.xiaoxun

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.xiaoxun.databinding.LayoutApieWelcomeActivityBinding
import com.example.xiaoxun.repo.WelcomeRepo
import com.example.xiaoxun.viewmodel.CheckLoginStatus
import com.example.xiaoxun.viewmodel.WelcomeViewModel
import com.xiaoxun.apie.common.ACCOUNT_LOGIN_ACTIVITY_PATH
import com.xiaoxun.apie.common.APP_WELCOME_ACTIVITY_PATH
import com.xiaoxun.apie.common.APP_XX_ACTIVITY_PATH
import com.xiaoxun.apie.common.HOME_INDEX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.alphaHide
import com.xiaoxun.apie.common.utils.alphaShow
import com.xiaoxun.apie.common.utils.setDebouncingClickListener

@Route(path = APP_WELCOME_ACTIVITY_PATH)
class APieWelcomeActivity : APieBaseBindingActivity<LayoutApieWelcomeActivityBinding>(
    LayoutApieWelcomeActivityBinding::inflate
) {

    private val viewModel: WelcomeViewModel by lazy { WelcomeViewModel() }

    private val repo: WelcomeRepo by lazy { WelcomeRepo(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initObserver()
    }

    override fun initializeView() {
        super.initializeView()
        binding.goLoginLayout.setDebouncingClickListener {
            if (viewModel.isNotLogin()) {
                ARouter.getInstance().build(ACCOUNT_LOGIN_ACTIVITY_PATH).navigation()
            }
        }

        binding.skipLogin.setDebouncingClickListener {
            viewModel.updateLoginStatus(CheckLoginStatus.Login)
        }

        binding.logoLayout.setDebouncingClickListener {
            ARouter.getInstance().build(APP_XX_ACTIVITY_PATH).navigation()
        }
    }

    private fun initData() {
        repo.checkLoginStatus()
    }

    private fun initObserver() {
        observe(viewModel.loginStatus) {
            when(it) {
                CheckLoginStatus.Start -> {
                    binding.checkLoginLoadingView.show()
                    binding.goLoginTip.alphaHide(200)
                }
                CheckLoginStatus.Login -> {
                    binding.checkLoginLoadingView.hide()
                    ARouter.getInstance().build(HOME_INDEX_ACTIVITY_PATH).navigation()
                    this.finish()
                }
                CheckLoginStatus.NotLogin -> {
                    binding.checkLoginLoadingView.hide()
                    binding.goLoginTip.alphaShow(200)
                }
                else -> {}
            }
        }
    }
}