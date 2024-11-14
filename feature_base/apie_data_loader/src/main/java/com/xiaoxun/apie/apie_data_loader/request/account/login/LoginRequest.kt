package com.xiaoxun.apie.apie_data_loader.request.account.login

import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager

/**
 * 登录请求
 */
class LoginRequest(
    private val loginRequestBody: LoginRequestBody
) : APieRequestParams<AccountModel>() {
    override fun apiService(version: String): Observable<BaseResponse<AccountModel>>? {
        return APieApiManager.getAccountAPIService().accountLogin(loginRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_LOGIN.name}_${loginRequestBody.pushId}"
    }

    override fun getVersion(data: AccountModel): String {
        return "${APieUrl.ACCOUNT_LOGIN.name}_${loginRequestBody.pushId}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_LOGIN.url
    }

    override fun needCache(data: AccountModel): Boolean {
        return true
    }
}