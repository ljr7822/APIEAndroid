package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.CREATE_DESIRE_URL
import com.xiaoxun.apie.apie_data_loader.EXCHANGE_DESIRE_URL
import com.xiaoxun.apie.apie_data_loader.GET_DESIRE_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.GET_DESIRE_GROUP_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesireRequestBody
import com.xiaoxun.apie.common_model.home_page.desire.CommonDesireRespModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APiDesireService {
    /** 创建愿望 */
    @POST(CREATE_DESIRE_URL)
    fun createDesire(@Body createDesireRequestBody: CreateDesireRequestBody): Observable<BaseResponse<DesireModel>>

    /** 获取愿望列表 */
    @GET(GET_DESIRE_BY_USER_ID_URL)
    fun getAllPlanByUserId(@Path("userId") userId: String): Observable<BaseResponse<DesireRespModel>>

    /** 兑换心愿 */
    @GET(EXCHANGE_DESIRE_URL)
    fun exchangeDesire(@Path("desireId") desireId: String): Observable<BaseResponse<DesireModel>>

    @POST(GET_DESIRE_GROUP_BY_USER_ID_URL)
    fun getAllPlanGroupByUserId(@Path("userId") userId: String): Observable<BaseResponse<DesireGroupRespModel>>
}