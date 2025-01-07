package com.xiaoxun.apie.apie_data_loader.request.plan

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class UpdatePlanCompletedCount(
    private val optType: Int,
    private val planId: String
): APieRequestParams<PlanModel>() {
    override fun apiService(version: String): Observable<BaseResponse<PlanModel>>? {
        return APieApiManager.getPlanAPIService().updatePlanCompletedCount(
            optType = optType,
            planId = planId
        )
    }

    override fun dataType(): String {
        return "${APieUrl.CREATE_PLAN.name}_${planId}"
    }

    override fun getVersion(data: PlanModel): String {
        return "${APieUrl.CREATE_PLAN.name}_${planId}"
    }

    override fun url(): String {
        return APieUrl.CREATE_PLAN.url
    }

    override fun needCache(data: PlanModel): Boolean {
        return false
    }
}