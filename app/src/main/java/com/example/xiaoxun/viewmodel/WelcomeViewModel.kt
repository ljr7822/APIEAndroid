package com.example.xiaoxun.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel

class WelcomeViewModel: APieBaseViewModel() {
    private var _loginStatus = MutableLiveData<CheckLoginStatus>()
    val loginStatus: MutableLiveData<CheckLoginStatus> get() = _loginStatus

    init {
        _loginStatus.value = CheckLoginStatus.Start
    }

    fun updateLoginStatus(status: CheckLoginStatus) {
        if (_loginStatus.value == status) return
        _loginStatus.postValue(status)
    }

    fun isNotLogin(): Boolean {
        return _loginStatus.value == CheckLoginStatus.NotLogin
    }

    fun isLogin(): Boolean {
        return _loginStatus.value == CheckLoginStatus.Login
    }
}

enum class CheckLoginStatus {
    Start,
    Login,
    NotLogin
}