package com.xiaoxun.apie.data_loader.loader

import com.xiaoxun.apie.data_loader.DataLoaderRepository
import io.reactivex.Observable


open class APieSimpleDataLoader<Data>(private val loaderName: String, private val repository: DataLoaderRepository, private val memoryCacheEnable: Boolean = true) : BaseDataLoader<Data>() {

    override fun memoryCacheEnable(): Boolean = memoryCacheEnable

    override fun getDiskCacheResourceObservable(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return repository.getCacheSource(params)
    }

    override fun getRemoteResourceObservable(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return repository.getRemoteSource(params)
    }

    override fun getMemoryCacheResourceObservable(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return repository.getMemorySource(params)
    }

    override fun getLoaderName(): String {
        return loaderName
    }

    override fun cacheDiskData(params: RequestParams<Data>, data: Data) {
        repository.cacheDiskData(params, data)
    }

    override fun cacheMemoryData(params: RequestParams<Data>, data: Data) {
        repository.cacheMemoryData(params, data)
    }
}