package com.xiaoxun.apie.apie_data_loader.loader

import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.loader.APieDataLoaderException
import com.xiaoxun.apie.data_loader.loader.IDataLoader
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.repository.source.RemoteDataSource
import com.xiaoxun.apie.data_loader.utils.DataLoaderLogger
import io.reactivex.Observable

private const val SUCCESS_CODE = 0

class APieRemoteDataSource(private val cacheManager: DataCacheManager) : RemoteDataSource() {
    private fun <Data> loadSource(params: APieRequestParams<Data>): Observable<BaseResponse<Data>> {
        return APieLocalVersionSource(cacheManager).getVersion(params).flatMap {
            DataLoaderLogger.i("${params.url()} get version is $it")
            params.apiService(it) ?: Observable.error(
                APieDataLoaderException(message = "please provider api service")
            )
        }.flatMap {
            if (it.code == SUCCESS_CODE) {
                Observable.just(it)
            } else {
                APieToast.showDialog(it.message ?: "it.request error")
                Observable.error(
                    APieDataLoaderException(
                        message = it.message ?: "it.request error",
                        errorCode = it.code ?: -1
                    )
                )
            }
        }
    }

    override fun <Data> loadSource(params: IDataLoader.RequestParams<Data>): Observable<BaseResponse<Data>> {
        if (params is APieRequestParams) {
            return loadSource(params)
        } else {
            return super.loadSource(params)
        }
    }
}