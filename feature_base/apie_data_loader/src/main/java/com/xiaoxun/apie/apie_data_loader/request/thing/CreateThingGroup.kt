package com.xiaoxun.apie.apie_data_loader.request.thing

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.storage.group.ThingGroupModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class CreateThingGroup(
    private val createThingGroupRequestBody: CreateThingGroupRequestBody
) : APieRequestParams<ThingGroupModel>() {
    override fun apiService(version: String): Observable<BaseResponse<ThingGroupModel>>? {
        return APieApiManager.getThingGroupAPIService().createGroup(createThingGroupRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.CREATE_THING_GROUP.name}_${createThingGroupRequestBody.createUserId}"
    }

    override fun getVersion(data: ThingGroupModel): String {
        return "${APieUrl.CREATE_THING_GROUP.name}_${createThingGroupRequestBody.createUserId}"
    }

    override fun url(): String {
        return APieUrl.CREATE_THING_GROUP.url
    }

    override fun needCache(data: ThingGroupModel): Boolean {
        return false
    }
}