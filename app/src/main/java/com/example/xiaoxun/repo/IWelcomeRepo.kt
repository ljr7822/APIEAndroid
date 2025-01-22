package com.example.xiaoxun.repo

interface IWelcomeRepo {
    /** 检查登录状态 */
    fun checkLoginStatus()

    /** 获取用户信息 */
    suspend fun getUserInfo(userId: String)

    /** 获取阿里云sts token */
    suspend fun getSTSToken()

    fun onCleared()
}