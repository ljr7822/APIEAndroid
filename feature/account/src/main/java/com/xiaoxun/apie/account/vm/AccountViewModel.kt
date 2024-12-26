package com.xiaoxun.apie.account.vm

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequestBody
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequestBody
import com.xiaoxun.apie.apie_data_loader.request.account.sms.SendSmsCode
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy

class AccountViewModel: APieBaseViewModel() {

    private var _currentLoginWayType = MutableLiveData<LoginWayType>()
    val currentLoginWayType: MutableLiveData<LoginWayType> get() = _currentLoginWayType

    init {
        _currentLoginWayType.value = LoginWayType.SMS_CODE
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
    /**
     * 使用密码登录
     */
    suspend fun loginByPassword(
        phoneNum: String,
        password: String,
    ): Result<BaseResponse<AccountModel>> {
        return executeResult {
            DataLoaderManager.instance.loginByPassword(
                LoginByPasswordRequest(LoginByPasswordRequestBody(phoneNum, password)),
                CacheStrategy.FORCE_NET
            )
        }
    }

    /**
     * 使用短信验证码登录
     */
    suspend fun loginBySmsCode(
        phoneNum: String,
        smsCode: String,
    ): Result<BaseResponse<AccountModel>> {
        return executeResult {
            DataLoaderManager.instance.loginBySmsCode(
                LoginBySmsCodeRequest(LoginBySmsCodeRequestBody(phoneNum, smsCode)),
                CacheStrategy.FORCE_NET
            )
        }
    }

    /**
     * 发送短信验证码
     */
    suspend fun sendSmsCode(
        phoneNum: String,
        userId: String = "",
    ): Result<BaseResponse<SmsCodeModel>> {
        return executeResult {
            DataLoaderManager.instance.sendSmsCode(
                SendSmsCode(phoneNum, userId),
                CacheStrategy.FORCE_NET
            )
        }
    }
}

