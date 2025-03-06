package com.xiaoxun.apie.apie_data_loader.request.account.user

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

/**
 * 修改密码
 */
class ChangePasswordParams(
    private val changePasswordRequestBody: ChangePasswordRequestBody
) : APieRequestParams<AccountModel>() {
    override fun apiService(version: String): Observable<BaseResponse<AccountModel>>? {
        return APieApiManager.getAccountAPIService().changePassword(changePasswordRequestBody)
    }

    override fun dataType(): String {
        return APieUrl.ACCOUNT_CHANGE_PASSWORD.name
    }

    override fun getVersion(data: AccountModel): String {
        return APieUrl.ACCOUNT_CHANGE_PASSWORD.name
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_CHANGE_PASSWORD.url
    }

    override fun needCache(data: AccountModel): Boolean {
        return false
    }
}