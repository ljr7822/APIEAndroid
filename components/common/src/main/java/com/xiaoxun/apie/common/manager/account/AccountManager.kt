package com.xiaoxun.apie.common.manager.account

import com.xiaoxun.apie.common.repo.AccountMMKVRepository

object AccountManager {

    fun isLogin(): Boolean {
        return getUserId().isNotEmpty() && getToken().isNotEmpty()
    }

    fun getUserId(): String {
        return AccountMMKVRepository.userId ?: ""
    }

    fun getToken(): String {
        return AccountMMKVRepository.token ?: ""
    }

    fun getUserName(): String {
        return AccountMMKVRepository.userName ?: ""
    }

}