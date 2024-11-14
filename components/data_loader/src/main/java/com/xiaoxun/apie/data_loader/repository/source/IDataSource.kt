package com.xiaoxun.apie.data_loader.repository.source

import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.loader.IDataLoader
import io.reactivex.Observable

interface IDataSource {
    fun <Data> loadSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>
}