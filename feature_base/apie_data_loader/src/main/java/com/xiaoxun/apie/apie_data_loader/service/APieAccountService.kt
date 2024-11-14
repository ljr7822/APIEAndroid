package com.xiaoxun.apie.apie_data_loader.service

import com.xiaoxun.apie.apie_data_loader.ACCOUNT_LOGIN_URL
import com.xiaoxun.apie.apie_data_loader.request.account.login.LoginRequestBody
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface APieAccountService {

    @POST(ACCOUNT_LOGIN_URL)
    fun accountLogin(@Body loginRequestBody: LoginRequestBody): Observable<BaseResponse<AccountModel>>

//    @POST(ACCOUNT_REGISTER_URL)
//    fun accountRegister(@Body requestBody: RegisterRequestBody): Observable<BaseResponse<AccountModel>>
//
//    @POST(ACCOUNT_BIND_PUSH_ID_URL)
//    fun accountBind(@Path(encoded = true, value = "pushId") pushId: String): Observable<BaseResponse<AccountModel>>
}