package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.ACCOUNT_GET_ALL_PLAN_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.CREATE_PLAN_URL
import com.xiaoxun.apie.apie_data_loader.GET_ALL_PLAN_GROUP_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupRespModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APiePlanService {

    @GET(ACCOUNT_GET_ALL_PLAN_BY_USER_ID_URL)
    fun getAllPlanByUserId(@Path("userId") userId: String): Observable<BaseResponse<PlanRespModel>>

    @GET(GET_ALL_PLAN_GROUP_BY_USER_ID_URL)
    fun getAllPlanGroupByUserId(@Path("userId") userId: String): Observable<BaseResponse<PlanGroupRespModel>>

    @POST(CREATE_PLAN_URL)
    fun createPlan(@Body createPlanRequestBody: CreatePlanRequestBody): Observable<BaseResponse<PlanModel>>
}