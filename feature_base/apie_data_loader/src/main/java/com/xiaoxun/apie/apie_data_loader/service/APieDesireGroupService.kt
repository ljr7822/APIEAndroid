package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.CREATE_DESIRE_GROUP_URL
import com.xiaoxun.apie.apie_data_loader.CREATE_GROUP_URL
import com.xiaoxun.apie.apie_data_loader.DELETE_GROUP_URL
import com.xiaoxun.apie.apie_data_loader.GET_ALL_PLAN_GROUP_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.GET_DESIRE_GROUP_BY_USER_ID_URL
import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesireGroupRequestBody
import com.xiaoxun.apie.apie_data_loader.request.group.CreatePlanGroupRequestBody
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupModel
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupRespModel
import com.xiaoxun.apie.common_model.home_page.group.DeleteGroupRespModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APieDesireGroupService {

    /** 获取所有心愿分组 */
    @GET(GET_DESIRE_GROUP_BY_USER_ID_URL)
    fun getAllDesireGroupByUserId(@Path("userId") userId: String): Observable<BaseResponse<DesireGroupRespModel>>

    /** 创建心愿分组 */
    @POST(CREATE_DESIRE_GROUP_URL)
    fun createGroup(@Body createDesireGroupRequestBody: CreateDesireGroupRequestBody): Observable<BaseResponse<DesireGroupModel>>

    /** 删除分组 */
    @GET(DELETE_GROUP_URL)
    fun deleteGroup(@Path("groupId") groupId: String): Observable<BaseResponse<DeleteGroupRespModel>>
}