package com.xiaoxun.apie.apie_data_loader.request.desire

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class CreateDesire(
    private val createDesireRequestBody: CreateDesireRequestBody
) : APieRequestParams<DesireModel>() {
    override fun apiService(version: String): Observable<BaseResponse<DesireModel>>? {
        return APieApiManager.getDesireAPIService().createDesire(createDesireRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.CREATE_PLAN.name}_${createDesireRequestBody.createUserId}"
    }

    override fun getVersion(data: DesireModel): String {
        return "${APieUrl.CREATE_PLAN.name}_${createDesireRequestBody.createUserId}"
    }

    override fun url(): String {
        return APieUrl.CREATE_PLAN.url
    }

    override fun needCache(data: DesireModel): Boolean {
        return false
    }
}