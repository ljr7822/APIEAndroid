package com.xiaoxun.apie.account.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.view_model.CommonLoadingState

class AccountViewModel: APieBaseViewModel() {

    private var _commonLoginStatus = MutableLiveData<CommonLoadingState>()
    val commonLoginStatus get() = _commonLoginStatus

    private var _currentLoginWayType = MutableLiveData<LoginWayType>()
    val currentLoginWayType: MutableLiveData<LoginWayType> get() = _currentLoginWayType

    private var _loadingStatus = MutableLiveData<Pair<LoadingState, TipMsg>>()
    val loadingState: MutableLiveData<Pair<LoadingState, TipMsg>> get() = _loadingStatus

    private var _sendSmsCodeStatus = MutableLiveData<SendSmsCodeStatus>()
    val sendSmsCodeStatus: MutableLiveData<SendSmsCodeStatus> get() = _sendSmsCodeStatus

    private var _inputDoneStatus = MutableLiveData<Boolean>()
    val inputDoneStatus: MutableLiveData<Boolean> get() = _inputDoneStatus

    init {
        _currentLoginWayType.value = LoginWayType.SMS_CODE
        _inputDoneStatus.value = false
        _loadingStatus.value = Pair(LoadingState.Initialization, TipMsg())
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
        getSmsCodeSuccess()
    }

    fun sendSmsCodeFailed() {
        _sendSmsCodeStatus.value = SendSmsCodeStatus.SendFailed
        getSmsCodeFailed()
    }

    fun markLoginLoading() {
        _loadingStatus.value = Pair(LoadingState.Loading, TipMsg("正在登录..."))
    }

    fun onLoginFailed(errorMsg: String) {
        _loadingStatus.value = Pair(LoadingState.Failed, TipMsg(errorMsg))
    }

    fun onLoginSuccess() {
        _loadingStatus.value = Pair(LoadingState.Success, TipMsg("登录成功"))
    }

    fun updateInputDoneStatus(isDone: Boolean) {
        if (_inputDoneStatus.value == isDone) return
        _inputDoneStatus.value = isDone
    }

    fun getSmsCodeStart() {
        _commonLoginStatus.value = CommonLoadingState.START
    }

    private fun getSmsCodeSuccess() {
        _commonLoginStatus.value = CommonLoadingState.SUCCESS
    }

    private fun getSmsCodeFailed() {
        _commonLoginStatus.value = CommonLoadingState.FAILED
    }

    fun isSmsLoginWayType(): Boolean {
        return _currentLoginWayType.value == LoginWayType.SMS_CODE
    }
}

data class TipMsg(val message: String = "")
