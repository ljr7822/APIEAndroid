package com.xiaoxun.apie.account_manager.repo

import io.reactivex.Observable

interface IAccountDBRepository {

    fun findAccountDataByUserId(userId: String): Observable<AccountDataDescriptor>

    fun findAccountTokenByUserId(userId: String): Observable<String>

    fun findAccountTokenByPhoneNum(phoneNumber: String): Observable<String>

    fun insertAccountData(data: AccountDataDescriptor)
}

data class AccountDataDescriptor(
    val userId: String,
    val userName: String,
    val phoneNumber: String,
    val token: String,
    var userPortrait: String? = "",
    var desc: String? = "",
    var sex: Int = 0,
    var address: String? = "",
    var grade: Int = 0,
    var userType: Int = 0
)