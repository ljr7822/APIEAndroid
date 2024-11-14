package com.xiaoxun.apie.data_loader.loader

import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class IDataLoader<Data> {
    protected val disposeList: CompositeDisposable = CompositeDisposable()
    abstract fun getLoaderName(): String

    /**
     * 是否支持预请求
     */
    open fun enablePreRequest(): Boolean {
        return false
    }

    open fun memoryCacheEnable(): Boolean = true

    abstract fun preRequest()

    abstract fun getData(
        params: RequestParams<Data>,
        dataCacheCacheStrategy: CacheStrategy = CacheStrategy.FIRST_CACHE
    ): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>

    /**
     * 获取磁盘缓存的数据，比如 DB 中缓存的数据
     */
    abstract fun getCacheData(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>

    /**
     * 获取内存缓存的数据
     */
    abstract fun getMemoryData(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>

    /**
     * 获取网络的数据
     */
    abstract fun getNetData(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>

    /**
     * 从 [disposeList] 移除添加的 Disposable 并 dispose 所有 item
     */
    open fun release() {
        disposeList.clear()
    }

    interface RequestParams<Data> {
        /** 外界注入的请求能力 */
        fun apiService(): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>? = null
        /** 数据类型，唯一标识，用于缓存的时候存储key，不同列表请用不同类型 */
        fun dataType(): String = ""
        /** 请求的url，主要是用于跟踪具体的url的请求链路 */
        fun url(): String = ""
        /** 是否需要缓存 */
        fun needCache(data: Data): Boolean = true
        fun subDir(): String = ""
        /** 缓存文件超时时间，不支持数据库和SP存储，单位为s，值>=10有效，避免误使用 */
        fun cacheTimeout() = 0L
    }

    interface ResponseParams
}