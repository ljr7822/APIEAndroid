package com.xiaoxun.apie.apie_data_loader.request.account.plan

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.plan.PlanRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class LoadPlans(
    private val userId: String
) : APieRequestParams<PlanRespModel>() {
    override fun apiService(version: String): Observable<BaseResponse<PlanRespModel>>? {
        return APieApiManager.getAccountAPIService().getAllPlanByUserId(userId)
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_GET_ALL_PLAN_BY_USER_ID.name}_${userId}"
    }

    override fun getVersion(data: PlanRespModel): String {
        return "${APieUrl.ACCOUNT_GET_ALL_PLAN_BY_USER_ID.name}_${userId}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_GET_ALL_PLAN_BY_USER_ID.url
    }

    override fun needCache(data: PlanRespModel): Boolean {
        return true
    }
}