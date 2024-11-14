package com.xiaoxun.apie.data_loader.repository.source

import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import com.xiaoxun.apie.data_loader.utils.DataLoaderLogger
import io.reactivex.Observable

class MemoryDataSource(private val dataCacheManager: DataCacheManager) : IDataSource {
    override fun <Data> loadSource(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return Observable.create {
            val value = dataCacheManager.memoryCache().get<Data>(params.dataType())
            if(it.isDisposed) {
                DataLoaderLogger.i("${params.url()} has disposed")
                return@create
            }
            if (value != null) {
                DataLoaderLogger.i("${params.url()}  获取内存数据 success")
                it.onNext(com.xiaoxun.apie.data_loader.data.BaseResponse<Data>().apply {
                    data = value
                })
                it.onComplete()
            } else {
                DataLoaderLogger.i("${params.url()} 内存数据为空")
                it.onError(Exception("内存数据为空"))
            }
        }
    }

    fun <Data> cacheData(params: com.xiaoxun.apie.data_loader.loader.IDataLoader.RequestParams<Data>, data: Data) {
        dataCacheManager.memoryCache().save(params.dataType(), data)
    }
}