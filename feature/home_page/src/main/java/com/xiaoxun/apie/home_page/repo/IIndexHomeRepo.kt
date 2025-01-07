package com.xiaoxun.apie.home_page.repo

import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.home_page.viewmodel.PlanListType
import com.xiaoxun.apie.home_page.viewmodel.PlanStatus

interface IIndexHomeRepo {

    /**
     * 计划类型筛选
     * @param planType 计划类型
     */
    suspend fun loadPlanByType(planType: PlanListType)

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
    suspend fun loadPlanGroup()

    /**
     * 更新计划完成次数
     */
    suspend fun updatePlanCompletedCount(optType: Int, planId: String)

    /**
     * 在生命周期结束时清理订阅
     */
    fun onCleared()
}