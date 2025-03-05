package com.xiaoxun.apie.account.repo

import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse

interface IAccountRepo {

    suspend fun startLoginByPassword(phoneNum: String, password: String)

    suspend fun startLoginBySmsCode(phoneNum: String, smsCode: String)

    suspend fun getSmsCode(phoneNum: String)

    /** 获取阿里云sts token */
    suspend fun getSTSToken()

    fun onCleared()
}