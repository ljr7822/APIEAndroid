package com.xiaoxun.apie.home_page.repo.mine

import com.xiaoxun.apie.home_page.bean.ChangePasswordInfo

interface IMineRepo {

    suspend fun loadUserInfo()

    suspend fun changePassword(changePasswordInfo: ChangePasswordInfo)

    fun onCleared()
}