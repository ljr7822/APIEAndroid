package com.xiaoxun.apie.apie_data_loader.request.thing

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class LoadThings(
    private val userId: String
) : APieRequestParams<ThingRespModel>() {
    override fun apiService(version: String): Observable<BaseResponse<ThingRespModel>>? {
        return APieApiManager.getThingService().getAllThingByUserId(userId)
    }

    override fun dataType(): String {
        return "${APieUrl.GET_THING_LIST.name}_${userId}"
    }

    override fun getVersion(data: ThingRespModel): String {
        return "${APieUrl.GET_THING_LIST.name}_${userId}"
    }

    override fun url(): String {
        return APieUrl.GET_THING_LIST.url
    }

    override fun needCache(data: ThingRespModel): Boolean {
        return true
    }
}