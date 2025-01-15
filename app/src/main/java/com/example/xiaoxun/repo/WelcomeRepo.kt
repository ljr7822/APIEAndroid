package com.example.xiaoxun.repo

import com.example.xiaoxun.viewmodel.CheckLoginStatus
import com.example.xiaoxun.viewmodel.WelcomeViewModel
import com.xiaoxun.apie.common.manager.account.AccountManager

class WelcomeRepo(private val viewModel: WelcomeViewModel) {

    fun checkLoginStatus() {
        if (AccountManager.isLogin()) {
            viewModel.updateLoginStatus(CheckLoginStatus.Login)
        } else {
            viewModel.updateLoginStatus(CheckLoginStatus.NotLogin)
        }
    }
}