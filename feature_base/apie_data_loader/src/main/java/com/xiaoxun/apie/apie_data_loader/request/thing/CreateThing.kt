package com.xiaoxun.apie.apie_data_loader.request.thing

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import kotlin.jvm.Throws

class CreateThing(
    private val createThingRequestBody: CreateThingRequestBody
) : APieRequestParams<ThingItemModel>() {
    override fun apiService(version: String): Observable<BaseResponse<ThingItemModel>>? {
        return APieApiManager.getThingService().createThing(createThingRequestBody)
    }

    override fun dataType(): String {
        return "${APieUrl.CREATE_THING.name}_${createThingRequestBody.createUserId}"
    }

    override fun getVersion(data: ThingItemModel): String {
        return "${APieUrl.CREATE_THING.name}_${createThingRequestBody.createUserId}"
    }

    override fun url(): String {
        return APieUrl.CREATE_THING.url
    }

    override fun needCache(data: ThingItemModel): Boolean {
        return false
    }
}