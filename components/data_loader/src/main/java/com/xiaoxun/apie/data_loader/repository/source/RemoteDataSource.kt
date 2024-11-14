package com.xiaoxun.apie.data_loader.repository.source

import io.reactivex.Observable

open class RemoteDataSource : IDataSource {
    override fun <Data> loadSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return params.apiService()?.flatMap {
            if (it.code == 1) {
                Observable.just(it)
            } else {
                Observable.error(Exception("it.request error"))
            }
        } ?: Observable.error(Exception("请提供 api service"))
    }
}