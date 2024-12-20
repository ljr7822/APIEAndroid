package com.xiaoxun.apie.apie_data_loader.request.account.login.smscode

import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager

/**
 * 验证码登录请求
 */
class LoginBySmsCodeRequest(
    private val loginBySmsCodeRequestBody: LoginBySmsCodeRequestBody
) : APieRequestParams<AccountModel>() {
    override fun apiService(version: String): Observable<BaseResponse<AccountModel>>? {
        return APieApiManager.getAccountAPIService().loginBySmsCode(loginBySmsCodeRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_LOGIN_SMS_CODE.name}_${loginBySmsCodeRequestBody.phoneNum}"
    }

    override fun getVersion(data: AccountModel): String {
        return "${APieUrl.ACCOUNT_LOGIN_SMS_CODE.name}_${loginBySmsCodeRequestBody.phoneNum}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_LOGIN_SMS_CODE.url
    }

    override fun needCache(data: AccountModel): Boolean {
        return true
    }
}