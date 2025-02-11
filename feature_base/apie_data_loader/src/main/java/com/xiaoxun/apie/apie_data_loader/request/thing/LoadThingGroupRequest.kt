package com.xiaoxun.apie.apie_data_loader.request.thing

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupRespModel
import com.xiaoxun.apie.common_model.home_page.storage.group.ThingGroupModel
import com.xiaoxun.apie.common_model.home_page.storage.group.ThingGroupRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class LoadThingGroupRequest(
    private val userId: String
) : APieRequestParams<ThingGroupRespModel>() {
    override fun apiService(version: String): Observable<BaseResponse<ThingGroupRespModel>>? {
        return APieApiManager.getThingGroupAPIService().getAllThingGroupByUserId(userId)
    }

    override fun dataType(): String {
        return "${APieUrl.GET_THING_GROUPS.name}_${userId}"
    }

    override fun getVersion(data: ThingGroupRespModel): String {
        return "${APieUrl.GET_THING_GROUPS.name}_${userId}"
    }

    override fun url(): String {
        return APieUrl.GET_THING_GROUPS.url
    }

    override fun needCache(data: ThingGroupRespModel): Boolean {
        return true
    }
}