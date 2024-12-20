package com.xiaoxun.apie.apie_data_loader.request.account.login.password

import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager

/**
 * 登录请求
 */
class LoginByPasswordRequest(
    private val loginByPasswordRequestBody: LoginByPasswordRequestBody
) : APieRequestParams<AccountModel>() {
    override fun apiService(version: String): Observable<BaseResponse<AccountModel>>? {
        return APieApiManager.getAccountAPIService().loginByPassword(loginByPasswordRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_LOGIN_PASSWORD.name}_${loginByPasswordRequestBody.phoneNum}"
    }

    override fun getVersion(data: AccountModel): String {
        return "${APieUrl.ACCOUNT_LOGIN_PASSWORD.name}_${loginByPasswordRequestBody.phoneNum}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_LOGIN_PASSWORD.url
    }

    override fun needCache(data: AccountModel): Boolean {
        return true
    }
}