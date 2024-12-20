package com.xiaoxun.apie.account.vm

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

    suspend fun sendSmsCode(
        phoneNum: String,
        userId: String,
    ): Result<BaseResponse<SmsCodeModel>> {
        return executeResult {
            DataLoaderManager.instance.sendSmsCode(
                SendSmsCode(phoneNum, userId),
                CacheStrategy.FORCE_NET
            )
        }
    }
}