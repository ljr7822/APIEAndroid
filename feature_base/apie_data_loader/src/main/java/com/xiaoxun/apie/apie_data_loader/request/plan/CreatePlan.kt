package com.xiaoxun.apie.apie_data_loader.request.plan

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class CreatePlan(
    private val createPlanRequestBody: CreatePlanRequestBody
) : APieRequestParams<PlanModel>() {
    override fun apiService(version: String): Observable<BaseResponse<PlanModel>>? {
        return APieApiManager.getPlanAPIService().createPlan(createPlanRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.CREATE_PLAN.name}_${createPlanRequestBody.createUserId}"
    }

    override fun getVersion(data: PlanModel): String {
        return "${APieUrl.CREATE_PLAN.name}_${createPlanRequestBody.createUserId}"
    }

    override fun url(): String {
        return APieUrl.CREATE_PLAN.url
    }

    override fun needCache(data: PlanModel): Boolean {
        return false
    }
}