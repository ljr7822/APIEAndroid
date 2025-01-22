package com.xiaoxun.apie.home_page.repo.mine

interface IMineRepo {

    suspend fun loadUserInfo()

    fun onCleared()
}