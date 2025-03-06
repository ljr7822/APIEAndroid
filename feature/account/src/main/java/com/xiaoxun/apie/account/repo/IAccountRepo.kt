package com.xiaoxun.apie.account.repo

import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse

interface IAccountRepo {

    /** 手机号+密码登录 */
    suspend fun startLoginByPassword(phoneNum: String, password: String)

    /** 手机号+验证码登录 */
    suspend fun startLoginBySmsCode(phoneNum: String, smsCode: String)

    /** 获取短信验证码 */
    suspend fun getSmsCode(phoneNum: String)

    /** 获取阿里云sts token */
    suspend fun getSTSToken()

    /** 获取加密公钥 */
    suspend fun getPublicKey()

    fun onCleared()
}