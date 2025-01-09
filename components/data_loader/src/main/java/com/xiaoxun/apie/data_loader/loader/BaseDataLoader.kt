package com.xiaoxun.apie.data_loader.loader

import androidx.annotation.WorkerThread
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import com.xiaoxun.apie.data_loader.utils.DataLoaderLogger
import com.xiaoxun.apie.data_loader.utils.ThreadUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.schedulers.Schedulers

abstract class BaseDataLoader<Data>: IDataLoader<Data>() {
    private fun prepareRequest(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return getRemoteResourceObservable(params)
            .observeOn(Schedulers.io())
    }

    override fun preRequest() {
        if (enablePreRequest()) {
            // todo 预请求 需要把requestParams 下沉
        }
    }

    override fun getData(params: RequestParams<Data>, dataCacheCacheStrategy: CacheStrategy): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        DataLoaderLogger.i("${params.url()}  开始获取接口数据, 策略:$dataCacheCacheStrategy $this")
        val observable = when (dataCacheCacheStrategy) {
            CacheStrategy.FIRST_CACHE -> getDataFirstCache(params)
            CacheStrategy.FIRST_NET -> getDataFirstNet(params)
            CacheStrategy.FORCE_CACHE -> getDataForceCache(params)
            CacheStrategy.FORCE_MEMORY_CACHE -> getDataForceMemoryCache(params)
            CacheStrategy.FORCE_NET -> getDataForceNet(params)
            CacheStrategy.FIRST_CACHE_AND_SEND_REQUEST -> getDataForceCacheAndSendRequest(params)
            CacheStrategy.FIRST_CACHE_AND_REQUEST_NET ->getDataFirstCacheAndSendNet(params)
        }.doFinally {
            DataLoaderLogger.d("${params.url()} getData final release $dataCacheCacheStrategy $this")
            release()
        }
        return observable
    }

    private fun getDataFirstCacheAndSendNet(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return getMemoryData(params)
            .onErrorResumeNext(ObservableSource { observer ->
                DataLoaderLogger.i("${params.url()}  获取内存缓存【失败】")
                DataLoaderLogger.i("${params.url()}  获取磁盘缓存【开始】")
                disposeList.add(handleDiskRequestResponse(getDiskCacheResourceObservable(params), params)
                    .subscribe({
                        observer.onNext(it)
                        DataLoaderLogger.i("${params.url()}  获取磁盘缓存【成功】")
                    },{
                        observer.onError(it)
                        DataLoaderLogger.i("${params.url()}  获取磁盘缓存【失败】")
                    },{
                        observer.onComplete()
                        DataLoaderLogger.i("${params.url()}  获取磁盘缓存【结束】")
                        disposeList.add(handleNetRequestResponse(getRemoteResourceObservable(params), params).subscribe({},{},{}))
                    }))
            })
            .onErrorResumeNext(ObservableSource { observer ->
                DataLoaderLogger.i("${params.url()}  获取磁盘缓存【失败】")
                DataLoaderLogger.i("${params.url()}  获取网络数据【开始】")
                disposeList.add(handleNetRequestResponse(getRemoteResourceObservable(params), params).subscribe({
                    observer.onNext(it)
                    DataLoaderLogger.i("${params.url()}  获取网络数据【成功】")
                },{
                    observer.onError(it)
                    DataLoaderLogger.i("${params.url()}  获取网络数据【失败】")
                },{
                    observer.onComplete()
                    DataLoaderLogger.i("${params.url()}  获取网络数据【结束】")
                }))
            })
    }


    private fun getDataForceCacheAndSendRequest(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return getMemoryData(params)
            .onErrorResumeNext(ObservableSource { observer ->
                DataLoaderLogger.i("${params.url()}  优先缓存，同时会发送一次请求 memory error")
                DataLoaderLogger.i("${params.url()}  优先缓存，同时会发送一次请求 disk and net start")
                disposeList.add(
                    Observable.zip(
                        handleNetRequestResponse(getRemoteResourceObservable(params), params)
                            .doOnNext {
                                DataLoaderLogger.i("${params.url()}  优先缓存，同时会发送一次请求 net success")
                                observer.onNext(it) }
                            .onErrorReturn {
                                DataLoaderLogger.e("${params.url()}  优先缓存，同时会发送一次请求 net error, $it")
                                com.xiaoxun.apie.data_loader.data.BaseResponse<Data>()
                                    .apply { code = 1 } },
                        handleDiskRequestResponse(getDiskCacheResourceObservable(params), params)
                            .doOnNext {
                                DataLoaderLogger.i("${params.url()}  优先缓存，同时会发送一次请求 disk success")
                                observer.onNext(it) }
                            .onErrorReturn {
                                DataLoaderLogger.i("${params.url()}  优先缓存，同时会发送一次请求 disk error, $it")
                                com.xiaoxun.apie.data_loader.data.BaseResponse<Data>()
                                    .apply { code = 0 } })
                    { t1, t2 -> t1.code == 1 || t2.code == 1 }.subscribe({
                        if (!it) {
                            observer.onError(
                                APieDataLoaderException(message = "优先缓存，同时会发送一次请求 error"))
                        } else {
                            DataLoaderLogger.i("${params.url()}  优先缓存，同时会发送一次请求 success")
                        }
                    }, {
                        observer.onError(it)
                    }, {
                        observer.onComplete()
                    }))
            })
    }

    private fun getDataFirstCache(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        DataLoaderLogger.i("${params.url()}  优先缓存 start")
        return getMemoryData(params)
            .onErrorResumeNext(ObservableSource { observer ->
                DataLoaderLogger.i("${params.url()}  内存缓存 error")
                DataLoaderLogger.i("${params.url()}  磁盘缓存 start")
                disposeList.add(
                    handleDiskRequestResponse(getDiskCacheResourceObservable(params), params)
                        .subscribe({
                            DataLoaderLogger.i("${params.url()}  磁盘缓存 success")
                            observer.onNext(it)
                        }, {
                            observer.onError(it)
                            DataLoaderLogger.e("${params.url()}  " + "磁盘缓存 error")
                        }, {
                            observer.onComplete()
                        })
                )
            })
            .onErrorResumeNext(ObservableSource { observer ->
                DataLoaderLogger.i("${params.url()}  网络请求 start")
                disposeList.add(
                    handleNetRequestResponse(getRemoteResourceObservable(params), params)
                        .subscribe({
                            observer.onNext(it)
                            DataLoaderLogger.i("${params.url()}  网络请求 success")
                        }, {
                            observer.onError(it)
                            DataLoaderLogger.i("${params.url()}  网络请求 error")
                        }, {
                            observer.onComplete()
                        })
                )
            }
            )
    }

    private fun getDataFirstNet(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        DataLoaderLogger.i("${params.url()}  优先网络 start")
        return handleNetRequestResponse(getRemoteResourceObservable(params), params)
            .onErrorResumeNext(ObservableSource { observer ->
                DataLoaderLogger.i("${params.url()}  优先网络 error")
                DataLoaderLogger.i("${params.url()}  缓存数据 start")
                disposeList.add(
                    handleDiskRequestResponse(getDiskCacheResourceObservable(params), params)
                        .subscribe({
                            observer.onNext(it)
                            DataLoaderLogger.i("${params.url()}  缓存数据 success")
                        }, {
                            observer.onError(it)
                            DataLoaderLogger.i("${params.url()}  缓存数据 error")
                        }, {
                            observer.onComplete()
                        })
                )
            })
    }

    private fun getDataForceCache(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        DataLoaderLogger.i("${params.url()}  强制缓存 start")
        return getMemoryData(params)
            .onErrorResumeNext(ObservableSource { observer ->
                DataLoaderLogger.i("${params.url()}  内存缓存 error")
                DataLoaderLogger.i("${params.url()}  缓存数据 start")
                disposeList.add(
                    handleDiskRequestResponse(getDiskCacheResourceObservable(params), params)
                        .subscribe({
                            observer.onNext(it)
                            DataLoaderLogger.i("${params.url()}  缓存数据 success")
                        }, {
                            observer.onError(it)
                            DataLoaderLogger.i("${params.url()}  缓存数据 error")
                        }, {
                            observer.onComplete()
                        })
                )
            })
    }

    private fun getDataForceMemoryCache(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        DataLoaderLogger.i("${params.url()}  强制内存缓存")
        return getMemoryData(params)
    }

    private fun getDataForceNet(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        DataLoaderLogger.i("${params.url()}  强制网络 start")
        return handleNetRequestResponse(getRemoteResourceObservable(params), params)
    }

    protected open fun persistentCacheData(data: Data, params: RequestParams<Data>) {
        ThreadUtils.runOnChildThread { cacheDiskData(params, data) }
    }

    override fun getCacheData(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return getDiskCacheResourceObservable(params)
    }

    override fun getMemoryData(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        DataLoaderLogger.i("${params.url()}  获取内存数据开始")
        return getMemoryCacheResourceObservable(params)
    }

    override fun getNetData(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return getRemoteResourceObservable(params)
    }

    /**
     * 该方法中会检测请求成功后缓存数据到[mLiveData]和 Disk 缓存中
     */
    private fun handleNetRequestResponse(
        observable: Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>,
        params: RequestParams<Data>
    ): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return observable
            .doOnNext { response ->
                val data = response.data
                data?.let {
                    if (params.needCache(it)) {
                        DataLoaderLogger.i("${params.url()}  处理网络请求响应：持久缓存数据")
                        persistentCacheData(data, params)
                        cacheInMemory(data, params)
                    }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
    }

    private fun handleDiskRequestResponse(
        observable: Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>,
        params: RequestParams<Data>
    ): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>> {
        return observable
            .doOnNext { response ->
                val data = response.data
                data?.let {
                    if (params.needCache(it)) {
                        DataLoaderLogger.i("${params.url()}  处理磁盘请求响应：持久缓存数据")
                        cacheInMemory(data, params)
                    }
                }
            }
            .doOnError {
                it.printStackTrace()
            }
    }

    private fun cacheInMemory(data: Data, params: RequestParams<Data>) {
        if (memoryCacheEnable()) {
            cacheMemoryData(params, data)
        }
    }

    /**
     * 获取本地缓存资源的Observable
     */
    protected abstract fun getDiskCacheResourceObservable(
        params: RequestParams<Data>
    ): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>

    /**
     * 获取线上资源请求的Observable
     */
    protected abstract fun getRemoteResourceObservable(
        params: RequestParams<Data>
    ): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>


    protected abstract fun getMemoryCacheResourceObservable(params: RequestParams<Data>): Observable<com.xiaoxun.apie.data_loader.data.BaseResponse<Data>>

    /**
     * 实现数据缓存，可以存储到数据库或Disk File
     */
    @WorkerThread
    protected open fun cacheDiskData(params: RequestParams<Data>, data: Data) {
    }

    protected open fun cacheMemoryData(params: RequestParams<Data>, data: Data){}
}