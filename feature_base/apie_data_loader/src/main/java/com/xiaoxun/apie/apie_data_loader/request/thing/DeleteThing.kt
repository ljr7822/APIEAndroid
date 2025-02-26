package com.xiaoxun.apie.apie_data_loader.request.thing

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.common_model.home_page.thing.DeleteThingRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import kotlin.jvm.Throws

class DeleteThing(
    private val thingId: String
) : APieRequestParams<DeleteThingRespModel>() {
    override fun apiService(version: String): Observable<BaseResponse<DeleteThingRespModel>>? {
        return APieApiManager.getThingService().deleteThing(thingId)
    }

    override fun dataType(): String {
        return "${APieUrl.DELETE_THING.name}_${thingId}"
    }

    override fun getVersion(data: DeleteThingRespModel): String {
        return "${APieUrl.DELETE_THING.name}_${thingId}"
    }

    override fun url(): String {
        return APieUrl.DELETE_THING.url
    }

    override fun needCache(data: DeleteThingRespModel): Boolean {
        return false
    }
}