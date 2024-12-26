package com.example.xiaoxun

import android.os.Bundle
import com.example.xiaoxun.databinding.LayoutApieWelcomeActivityBinding
import com.example.xiaoxun.repo.WelcomeRepo
import com.example.xiaoxun.viewmodel.CheckLoginStatus
import com.example.xiaoxun.viewmodel.WelcomeViewModel
import com.gyf.immersionbar.ImmersionBar
import com.xiaoxun.apie.account.activity.LoginActivity
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.alphaHide
import com.xiaoxun.apie.common.utils.alphaShow
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show

/**
 * 欢迎页
 */
class APieWelcomeActivity : APieBaseBindingActivity<LayoutApieWelcomeActivityBinding>(
    LayoutApieWelcomeActivityBinding::inflate
) {

    private val viewModel: WelcomeViewModel by lazy { WelcomeViewModel() }

    private val repo: WelcomeRepo by lazy { WelcomeRepo(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowsStyle()
        initData()
        initObserver()
    }

    private fun initWindowsStyle() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
    }

    override fun initializeView() {
        super.initializeView()
        binding.goLoginLayout.setDebouncingClickListener {
            if (viewModel.isNotLogin()) {
                LoginActivity.start(this)
            }
        }

        binding.skipLogin.setDebouncingClickListener {
            viewModel.updateLoginStatus(CheckLoginStatus.Login)
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
                    APieHomeActivity.start(this)
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