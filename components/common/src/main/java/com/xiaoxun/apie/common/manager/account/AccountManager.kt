package com.xiaoxun.apie.common.manager.account

import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.utils.cache.APieCacheUtils

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

    fun logout() {
        AccountMMKVRepository.clearAllKV()
    }
}