package com.xiaoxun.apie.apie_data_loader.request.account.sms

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.sms.STSTokenModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

/**
 * 获取token
 */
class STSTokenParams(
    private val userId: String,
) : APieRequestParams<STSTokenModel>() {
    override fun apiService(version: String): Observable<BaseResponse<STSTokenModel>>? {
        return APieApiManager.getAccountAPIService().getSTSToken()
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_GET_STS_TOKEN.name}_${userId}"
    }

    override fun getVersion(data: STSTokenModel): String {
        return "${APieUrl.ACCOUNT_GET_STS_TOKEN.name}_${userId}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_GET_STS_TOKEN.url
    }

    override fun needCache(data: STSTokenModel): Boolean {
        return false
    }
}