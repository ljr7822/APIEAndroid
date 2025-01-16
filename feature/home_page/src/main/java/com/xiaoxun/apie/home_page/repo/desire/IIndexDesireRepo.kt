package com.xiaoxun.apie.home_page.repo.desire

import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesireRequestBody

interface IIndexDesireRepo {

    suspend fun createDesire(createDesireRequestBody: CreateDesireRequestBody)

    suspend fun loadDesireList(userId: String?, manualRefresh: Boolean = false)

    /**
     * 在生命周期结束时清理订阅
     */
    fun onCleared()
}