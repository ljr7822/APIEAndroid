package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.ACCOUNT_GET_ALL_PLAN_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.CREATE_DESIRE_URL
import com.xiaoxun.apie.apie_data_loader.CREATE_GROUP_URL
import com.xiaoxun.apie.apie_data_loader.CREATE_PLAN_URL
import com.xiaoxun.apie.apie_data_loader.DELETE_GROUP_URL
import com.xiaoxun.apie.apie_data_loader.DELETE_PLAN_URL
import com.xiaoxun.apie.apie_data_loader.GET_ALL_PLAN_GROUP_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.GET_DESIRE_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.UPDATE_PLAN_COMPLETED_COUNT_URL
import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesireRequestBody
import com.xiaoxun.apie.apie_data_loader.request.group.CreatePlanGroupRequestBody
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.group.DeleteGroupRespModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupRespModel
import com.xiaoxun.apie.common_model.home_page.plan.DeletePlanRespModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APiePlanService {

    /** 获取用户所有计划 */
    @GET(ACCOUNT_GET_ALL_PLAN_BY_USER_ID_URL)
    fun getAllPlanByUserId(@Path("userId") userId: String): Observable<BaseResponse<PlanRespModel>>

    /** 创建计划 */
    @POST(CREATE_PLAN_URL)
    fun createPlan(@Body createPlanRequestBody: CreatePlanRequestBody): Observable<BaseResponse<PlanModel>>

    /** 更新计划完成次数 */
    @GET(UPDATE_PLAN_COMPLETED_COUNT_URL)
    fun updatePlanCompletedCount(@Path("optType") optType: Int, @Path("planId") planId: String): Observable<BaseResponse<PlanModel>>

    /** 删除计划 */
    @GET(DELETE_PLAN_URL)
    fun deletePlan(@Path("planId") planId: String): Observable<BaseResponse<DeletePlanRespModel>>
}