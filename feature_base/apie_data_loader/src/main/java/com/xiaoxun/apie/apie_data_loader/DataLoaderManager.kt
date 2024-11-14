package com.xiaoxun.apie.apie_data_loader

import android.content.Context
import com.xiaoxun.apie.apie_data_loader.repository.APieDataLoaderRepository
import com.xiaoxun.apie.apie_data_loader.request.account.login.LoginRequest
import com.xiaoxun.apie.common_model.account.AccountModel
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
     * 登录接口
     */
    fun login(loginRequestParams: LoginRequest, cacheStrategy: CacheStrategy): Observable<BaseResponse<AccountModel>> {
        val loader = buildDataLoader<AccountModel>(APieUrl.ACCOUNT_LOGIN.name)
        return loader?.getData(loginRequestParams, cacheStrategy) ?: Observable.error(Exception("loader is null"))
    }

//    /**
//     * 注册接口
//     */
//    fun register(registerRequestParams: RegisterRequest, cacheStrategy: CacheStrategy): Observable<BaseResponse<AccountModel>> {
//        val loader = buildDataLoader<AccountModel>(APicUrl.ACCOUNT_REGISTER.name)
//        return loader?.getData(registerRequestParams, cacheStrategy) ?: Observable.error(Exception("loader is null"))
//    }
}