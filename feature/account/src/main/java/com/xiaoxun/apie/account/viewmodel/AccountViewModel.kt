package com.xiaoxun.apie.account.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel

class AccountViewModel: APieBaseViewModel() {

    private var _currentLoginWayType = MutableLiveData<LoginWayType>()
    val currentLoginWayType: MutableLiveData<LoginWayType> get() = _currentLoginWayType

    private var _loadingStatus = MutableLiveData<LoadingState>()
    val loadingState: MutableLiveData<LoadingState> get() = _loadingStatus

    private var _sendSmsCodeStatus = MutableLiveData<SendSmsCodeStatus>()
    val sendSmsCodeStatus: MutableLiveData<SendSmsCodeStatus> get() = _sendSmsCodeStatus

    init {
        _currentLoginWayType.value = LoginWayType.SMS_CODE
        _loadingStatus.value = LoadingState.Initialization
    }

    fun updateLoginWayType(type: LoginWayType) {
        if (_currentLoginWayType.value == type) return
        _currentLoginWayType.value = type
    }

    fun isLoginByPassword(): Boolean {
        return _currentLoginWayType.value == LoginWayType.PASSWORD
    }

    fun isLoginBySmsCode(): Boolean {
        return _currentLoginWayType.value == LoginWayType.SMS_CODE
    }

    fun switchLoginWay() {
        _currentLoginWayType.value = if (isLoginByPassword()) LoginWayType.SMS_CODE else LoginWayType.PASSWORD
    }

    fun sendSmsCodeSuccess() {
        _sendSmsCodeStatus.value = SendSmsCodeStatus.SendSuccess
    }

    fun sendSmsCodeFailed() {
        _sendSmsCodeStatus.value = SendSmsCodeStatus.SendFailed
    }

    fun markLoginLoading() {
        _loadingStatus.value = LoadingState.Loading
    }

    fun onLoginFail() {
        _loadingStatus.value = LoadingState.Failed
    }

    fun onLoginSuccess() {
        _loadingStatus.value = LoadingState.Success
    }
}

