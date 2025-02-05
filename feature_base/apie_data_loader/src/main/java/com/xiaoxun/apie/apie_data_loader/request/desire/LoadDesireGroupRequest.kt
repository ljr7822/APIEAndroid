package com.xiaoxun.apie.apie_data_loader.request.desire

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class LoadDesireGroupRequest(
    private val userId: String
) : APieRequestParams<DesireGroupRespModel>() {
    override fun apiService(version: String): Observable<BaseResponse<DesireGroupRespModel>>? {
        return APieApiManager.getDesireGroupAPIService().getAllDesireGroupByUserId(userId)
    }

    override fun dataType(): String {
        return "${APieUrl.GET_DESIRE_GROUP_BY_USER_ID.name}_${userId}"
    }

    override fun getVersion(data: DesireGroupRespModel): String {
        return "${APieUrl.GET_DESIRE_GROUP_BY_USER_ID.name}_${userId}"
    }

    override fun url(): String {
        return APieUrl.GET_DESIRE_GROUP_BY_USER_ID.url
    }

    override fun needCache(data: DesireGroupRespModel): Boolean {
        return true
    }
}