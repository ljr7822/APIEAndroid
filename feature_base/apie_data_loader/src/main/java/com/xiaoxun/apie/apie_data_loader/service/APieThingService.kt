package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.GET_THING_LIST_URL
import com.xiaoxun.apie.common_model.home_page.storage.ThingRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface APieThingService {

    /** 获取所有物品 */
    @GET(GET_THING_LIST_URL)
    fun getAllThingByUserId(@Path("userId") userId: String): Observable<BaseResponse<ThingRespModel>>
}