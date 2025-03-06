package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.ACCOUNT_GET_PUBLIC_KEY_URL
import com.xiaoxun.apie.apie_data_loader.ACCOUNT_GET_STS_TOKEN_URL
import com.xiaoxun.apie.apie_data_loader.ACCOUNT_GET_USER_INFO_URL
import com.xiaoxun.apie.apie_data_loader.ACCOUNT_LOGIN_PASSWORD_URL
import com.xiaoxun.apie.apie_data_loader.ACCOUNT_LOGIN_SMS_CODE_URL
import com.xiaoxun.apie.apie_data_loader.ACCOUNT_SEND_SMS_CODE_URL
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequestBody
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequestBody
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.account.PublicKeyModel
import com.xiaoxun.apie.common_model.sms.STSTokenModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APieAccountService {

    @POST(ACCOUNT_LOGIN_PASSWORD_URL)
    fun loginByPassword(@Body loginByPasswordRequestBody: LoginByPasswordRequestBody): Observable<BaseResponse<AccountModel>>

    @POST(ACCOUNT_LOGIN_SMS_CODE_URL)
    fun loginBySmsCode(@Body loginBySmsCodeRequestBody: LoginBySmsCodeRequestBody): Observable<BaseResponse<AccountModel>>

    @GET(ACCOUNT_SEND_SMS_CODE_URL)
    fun sendSmsCode(@Path("phoneNum") phoneNum: String): Observable<BaseResponse<SmsCodeModel>>

    @GET(ACCOUNT_GET_STS_TOKEN_URL)
    fun getSTSToken(): Observable<BaseResponse<STSTokenModel>>

    @GET(ACCOUNT_GET_PUBLIC_KEY_URL)
    fun getPublicKey(): Observable<BaseResponse<PublicKeyModel>>

    @GET(ACCOUNT_GET_USER_INFO_URL)
    fun getUserInfo(@Path("userId") userId: String): Observable<BaseResponse<AccountModel>>
}