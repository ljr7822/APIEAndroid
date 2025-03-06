package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.view_model.CommonLoadingState

class IndexMineViewModel: APieBaseViewModel()  {

    // 通用的loading状态
    private var _commonLoadingState = MutableLiveData<CommonLoadingState>()
    val commonLoadingState get() = _commonLoadingState

    // 个人信息
    private var _userInfo = MutableLiveData<AccountModel>()
    val userInfo get() = _userInfo

    fun onLoadInfoStart() {
        _commonLoadingState.value = CommonLoadingState.START
    }

    fun onLoadInfoSuccess(userInfo: AccountModel) {
        _commonLoadingState.value = CommonLoadingState.SUCCESS
        _userInfo.value = userInfo
    }

    fun onLoadInfoFailed() {
        _commonLoadingState.value = CommonLoadingState.FAILED
    }

    fun onLoadingStart() {
        _commonLoadingState.value = CommonLoadingState.START
    }

    fun onLoadingSuccess() {
        _commonLoadingState.value = CommonLoadingState.SUCCESS
    }

    fun onLoadingFailed() {
        _commonLoadingState.value = CommonLoadingState.FAILED
    }
}