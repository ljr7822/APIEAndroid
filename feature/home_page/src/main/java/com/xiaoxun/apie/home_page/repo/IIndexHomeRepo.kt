package com.xiaoxun.apie.home_page.repo

import com.xiaoxun.apie.home_page.viewmodel.PlanListType

interface IIndexHomeRepo {

    /**
     * 筛选时使用加载计划列表
     * @param planType 筛选条件
     */
    suspend fun loadPlanListByType(planType: PlanListType)

    /**
     * 通过计划id加载计划详情
     */
    suspend fun loadPlanDetail(planId: String)

    /**
     * 创建计划
     */
    suspend fun createPlan()

    /**
     * 在生命周期结束时清理订阅
     */
    fun onCleared()
}