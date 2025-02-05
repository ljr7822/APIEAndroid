package com.xiaoxun.apie.apie_data_loader.request.desire

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class CreateDesireGroup(
    private val createDesireGroupRequestBody: CreateDesireGroupRequestBody
) : APieRequestParams<DesireGroupModel>() {
    override fun apiService(version: String): Observable<BaseResponse<DesireGroupModel>>? {
        return APieApiManager.getDesireGroupAPIService().createGroup(createDesireGroupRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.CREATE_DESIRE_GROUP.name}_${createDesireGroupRequestBody.createUserId}"
    }

    override fun getVersion(data: DesireGroupModel): String {
        return "${APieUrl.CREATE_DESIRE_GROUP.name}_${createDesireGroupRequestBody.createUserId}"
    }

    override fun url(): String {
        return APieUrl.CREATE_DESIRE_GROUP.url
    }

    override fun needCache(data: DesireGroupModel): Boolean {
        return false
    }
}