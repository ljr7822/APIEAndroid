package com.xiaoxun.apie.apie_data_loader.request

import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.loader.IDataLoader
import com.xiaoxun.apie.data_loader.utils.DataLoaderLogger
import io.reactivex.Observable

abstract class APieRequestParams <Data> : IDataLoader.RequestParams<Data> {
    /** 提供缓存的version的字段 */
    abstract fun apiService(version: String): Observable<BaseResponse<Data>>?

    final override fun apiService(): Observable<BaseResponse<Data>>? {
        DataLoaderLogger.e("do not use this, instead of apiService(version: String) ")
        return null
    }

    /** 获取version字段 */
    open fun getVersion(data: Data): String = ""

    abstract override fun dataType(): String
}