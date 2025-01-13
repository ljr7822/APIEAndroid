package com.xiaoxun.apie.apie_data_loader.request.group

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class CreatePlanGroup(
    private val createPlanGroupRequestBody: CreatePlanGroupRequestBody
) : APieRequestParams<PlanGroupModel>() {
    override fun apiService(version: String): Observable<BaseResponse<PlanGroupModel>>? {
        return APieApiManager.getPlanAPIService().createGroup(createPlanGroupRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.CREATE_GROUP.name}_${createPlanGroupRequestBody.createUserId}"
    }

    override fun getVersion(data: PlanGroupModel): String {
        return "${APieUrl.CREATE_GROUP.name}_${createPlanGroupRequestBody.createUserId}"
    }

    override fun url(): String {
        return APieUrl.CREATE_GROUP.url
    }

    override fun needCache(data: PlanGroupModel): Boolean {
        return false
    }
}