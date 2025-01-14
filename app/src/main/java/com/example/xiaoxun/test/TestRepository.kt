package com.example.xiaoxun.test

import com.example.xiaoxun.test.TestRepository.intPreference
import com.xiaoxun.apie.common.datastore.DataStoreOwner

object TestRepository : DataStoreOwner("account-store") {
    val userName by stringPreference()
    val userId by stringPreference()
    val token by stringPreference()
    val phoneNum by stringPreference()
}
