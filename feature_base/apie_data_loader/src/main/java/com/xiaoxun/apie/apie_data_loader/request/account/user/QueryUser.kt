package com.xiaoxun.apie.apie_data_loader.request.account.user

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class QueryUser(
    private val userId: String
) : APieRequestParams<AccountModel>() {
    override fun apiService(version: String): Observable<BaseResponse<AccountModel>>? {
        return APieApiManager.getAccountAPIService().getUserInfo(
            userId = userId
        )
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_GET_USER_INFO.name}_${userId}"
    }

    override fun getVersion(data: AccountModel): String {
        return "${APieUrl.ACCOUNT_GET_USER_INFO.name}_${userId}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_GET_USER_INFO.url
    }

    override fun needCache(data: AccountModel): Boolean {
        return false
    }
}
