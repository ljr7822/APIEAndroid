package com.xiaoxun.apie.home_page.repo.home

import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.apie_data_loader.request.group.UpdateGroupRequestBody
import com.xiaoxun.apie.common_model.view_model.CompletedCountOptType
import com.xiaoxun.apie.common_model.view_model.PlanListType
import com.xiaoxun.apie.common_model.view_model.PlanStatus

interface IIndexHomeRepo {

    /**
     * 计划类型筛选
     * @param planType 计划类型
     */
    suspend fun loadPlanByType(planType: PlanListType, manualRefresh: Boolean = false)

    /**
     * 筛选时使用加载计划列表
     * @param status 筛选条件
     */
    suspend fun loadPlanByStatus(status: PlanStatus)

    /**
     * 通过计划id加载计划详情
     */
    suspend fun loadPlanDetail(planId: String)

    /**
     * 创建计划
     */
    suspend fun createPlan(createPlanRequestBody: CreatePlanRequestBody)

    /**
     * 获取用户分组信息
     */
    suspend fun loadPlanGroupList()

    /**
     * 创建一个新分组
     */
    suspend fun createPlanGroup(groupName: String)

    /**
     * 更新计划完成次数
     */
    suspend fun updatePlanCompletedCount(optType: CompletedCountOptType, planId: String)

    /**
     * 删除计划
     */
    suspend fun deletePlan(planId: String)

    /**
     * 删除分组
     */
    suspend fun deleteGroup(groupId: String)

    /**
     * 更新分组数据
     */
    suspend fun updateGroup(updateGroupRequestBody: UpdateGroupRequestBody)

    /**
     * 在生命周期结束时清理订阅
     */
    fun onCleared()
}