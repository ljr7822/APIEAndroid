package com.xiaoxun.apie.apie_data_loader.request.group

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class LoadPlanGroups(
    private val userId: String
) : APieRequestParams<PlanGroupRespModel>() {
    override fun apiService(version: String): Observable<BaseResponse<PlanGroupRespModel>>? {
        return APieApiManager.getGroupAPIService().getAllPlanGroupByUserId(userId)
    }

    override fun dataType(): String {
        return "${APieUrl.GET_ALL_PLAN_GROUP_BY_USER_ID.name}_${userId}"
    }

    override fun getVersion(data: PlanGroupRespModel): String {
        return "${APieUrl.GET_ALL_PLAN_GROUP_BY_USER_ID.name}_${userId}"
    }

    override fun url(): String {
        return APieUrl.GET_ALL_PLAN_GROUP_BY_USER_ID.url
    }

    override fun needCache(data: PlanGroupRespModel): Boolean {
        return true
    }
}