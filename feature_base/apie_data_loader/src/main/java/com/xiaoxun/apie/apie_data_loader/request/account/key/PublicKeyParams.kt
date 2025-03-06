package com.xiaoxun.apie.apie_data_loader.request.account.key

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.account.PublicKeyModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

/**
 * 获取token
 */
class PublicKeyParams : APieRequestParams<PublicKeyModel>() {
    override fun apiService(version: String): Observable<BaseResponse<PublicKeyModel>>? {
        return APieApiManager.getAccountAPIService().getPublicKey()
    }

    override fun dataType(): String {
        return APieUrl.ACCOUNT_GET_PUBLIC_KEY.name
    }

    override fun getVersion(data: PublicKeyModel): String {
        return APieUrl.ACCOUNT_GET_PUBLIC_KEY.name
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_GET_PUBLIC_KEY.url
    }

    override fun needCache(data: PublicKeyModel): Boolean {
        return false
    }
}