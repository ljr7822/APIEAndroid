package com.xiaoxun.apie.apie_data_loader.repository

import com.xiaoxun.apie.apie_data_loader.loader.APieLocalVersionSource
import com.xiaoxun.apie.apie_data_loader.loader.APieRemoteDataSource
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.data_loader.DataLoaderRepositoryImp
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.loader.IDataLoader
import com.xiaoxun.apie.data_loader.repository.cache.DataCacheManager
import io.reactivex.Observable

class APieDataLoaderRepository (private val cacheManager: DataCacheManager) : DataLoaderRepositoryImp(cacheManager) {
    override fun <Data> getRemoteSource(params: IDataLoader.RequestParams<Data>): Observable<BaseResponse<Data>> {
        return APieRemoteDataSource(cacheManager).loadSource(params)
    }

    override fun <Data> cacheDiskData(params: IDataLoader.RequestParams<Data>, data: Data) {
        super.cacheDiskData(params, data)
        if (params is APieRequestParams) {
            APieLocalVersionSource(cacheManager).saveVersion(params.getVersion(data) ?: "", params)
        }
    }
}