package com.xiaoxun.apie.data_loader

import io.reactivex.Observable

open class DataLoaderRepositoryImp(private val cacheManager: com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager) :
    DataLoaderRepository {
    override fun <Data> getRemoteSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return com.xiaoxun.apie.data_loader.repository.source.RemoteDataSource().loadSource(params)
    }

    override fun <Data> getCacheSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return com.xiaoxun.apie.data_loader.repository.source.LocalDataSource(cacheManager).loadSource(params)
    }

    override fun <Data> getMemorySource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return com.xiaoxun.apie.data_loader.repository.source.MemoryDataSource(cacheManager).loadSource(params)
    }

    override fun <Data> cacheDiskData(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>, data: Data) {
        com.xiaoxun.apie.data_loader.repository.source.LocalDataSource(cacheManager)
            .cacheData(params, data)
    }

    override fun <Data> cacheMemoryData(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>, data: Data) {
        com.xiaoxun.apie.data_loader.repository.source.MemoryDataSource(cacheManager)
            .cacheData(params, data)
    }
}