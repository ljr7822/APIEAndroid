package com.xiaoxun.apie.apie_data_loader

import android.content.Context
import com.xiaoxun.apie.apie_data_loader.repository.APieDataLoaderRepository
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequest
import com.xiaoxun.apie.apie_data_loader.request.account.sms.SendSmsCode
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.loader.APieSimpleDataLoader
import com.xiaoxun.apie.data_loader.loader.IDataLoader
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import io.reactivex.Observable

class DataLoaderManager private constructor() {
    private var repository: APieDataLoaderRepository? = null

    companion object {
        const val TAG = "DataLoaderManager"
        val instance: DataLoaderManager by lazy { DataLoaderManager() }
    }

    fun initWithContext(context: Context) {
        val dataCacheManager = DataCacheManager(context)
        repository = APieDataLoaderRepository(dataCacheManager)
    }

    private fun <Data> buildDataLoader(
        loaderName: String,
        memoryCacheEnable: Boolean = true
    ): IDataLoader<Data>? =
        repository?.let { APieSimpleDataLoader<Data>(loaderName, it, memoryCacheEnable) }

    private fun <T> error(): Observable<T> {
        return Observable.error<T>(
            Exception("loader is null, please call registerLoader in DataLoaderManager#initWithContext()")
        )
    }

    /**
     * 密码登录接口
     */
    fun loginByPassword(loginByPasswordRequestParams: LoginByPasswordRequest, cacheStrategy: CacheStrategy): Observable<BaseResponse<AccountModel>> {
        val loader = buildDataLoader<AccountModel>(APieUrl.ACCOUNT_LOGIN_PASSWORD.name)
        return loader?.getData(loginByPasswordRequestParams, cacheStrategy) ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 验证码登录接口
     */
    fun loginBySmsCode(loginBySmsCodeRequestParams: LoginBySmsCodeRequest, cacheStrategy: CacheStrategy): Observable<BaseResponse<AccountModel>> {
        val loader = buildDataLoader<AccountModel>(APieUrl.ACCOUNT_LOGIN_SMS_CODE.name)
        return loader?.getData(loginBySmsCodeRequestParams, cacheStrategy) ?: Observable.error(Exception("loader is null"))
    }

    /**
     * 发送验证码接口
     */
    fun sendSmsCode(sendSmsCode: SendSmsCode, cacheStrategy: CacheStrategy): Observable<BaseResponse<SmsCodeModel>> {
        val loader = buildDataLoader<SmsCodeModel>(APieUrl.ACCOUNT_SEND_SMS_CODE.name)
        return loader?.getData(sendSmsCode, cacheStrategy) ?: Observable.error(Exception("loader is null"))
    }
}