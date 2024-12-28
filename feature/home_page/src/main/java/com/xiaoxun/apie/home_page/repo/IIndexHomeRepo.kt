package com.xiaoxun.apie.home_page.repo

interface IIndexHomeRepo {
    suspend fun loadPlanList()
    suspend fun loadPlanDetail(planId: String)
    suspend fun createPlan()
    fun onCleared()
}