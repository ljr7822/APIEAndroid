package com.xiaoxun.apie.data_loader

import io.reactivex.Observable

interface DataLoaderRepository {
    fun <Data> getRemoteSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>
    fun <Data> getCacheSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>
    fun <Data> getMemorySource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>
    fun <Data> cacheDiskData(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>, data: Data)
    fun <Data> cacheMemoryData(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>, data: Data)
}