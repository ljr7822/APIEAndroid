package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.CREATE_THING_URL
import com.xiaoxun.apie.apie_data_loader.DELETE_THING_URL
import com.xiaoxun.apie.apie_data_loader.GET_THING_LIST_URL
import com.xiaoxun.apie.apie_data_loader.request.thing.CreateThingRequestBody
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingRespModel
import com.xiaoxun.apie.common_model.home_page.thing.DeleteThingRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APieThingService {

    /** 获取所有物品 */
    @GET(GET_THING_LIST_URL)
    fun getAllThingByUserId(@Path("userId") userId: String): Observable<BaseResponse<ThingRespModel>>

    /** 创建一个物品 */
    @POST(CREATE_THING_URL)
    fun createThing(@Body createThingRequestBody: CreateThingRequestBody): Observable<BaseResponse<ThingItemModel>>

    /** 删除一个物品 */
    @GET(DELETE_THING_URL)
    fun deleteThing(@Path("thingId") thingId: String): Observable<BaseResponse<DeleteThingRespModel>>
}