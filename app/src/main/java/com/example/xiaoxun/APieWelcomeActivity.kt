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
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.AndroidUtils
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show

@Route(path = APP_WELCOME_ACTIVITY_PATH)
class APieWelcomeActivity : APieBaseBindingActivity<LayoutApieWelcomeActivityBinding>(
    LayoutApieWelcomeActivityBinding::inflate
) {

    private val viewModel: WelcomeViewModel by lazy { WelcomeViewModel() }

    private val repo: WelcomeRepo by lazy { WelcomeRepo(this, this, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initObserver()
    }

    override fun initializeView() {
        super.initializeView()
        val versionName = AndroidUtils.getVersionName(this)
        binding.versionTv.text = versionName
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
                    binding.goLoginLayout.hide()
                    binding.skipLogin.hide()
                }
                CheckLoginStatus.Login -> {
                    APieLog.d(TAG, "校验登录通过, 当前线程=${Thread.currentThread().name}")
                    binding.checkLoginLoadingView.hide()
                    ARouter.getInstance().build(HOME_INDEX_ACTIVITY_PATH).navigation()
                    this.finish()
                }
                CheckLoginStatus.NotLogin -> {
                    binding.checkLoginLoadingView.hide()
                    binding.goLoginLayout.show()
                    binding.skipLogin.show()
                }
                else -> {}
            }
        }
    }
}