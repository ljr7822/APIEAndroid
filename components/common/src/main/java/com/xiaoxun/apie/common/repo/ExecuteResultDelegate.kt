package com.xiaoxun.apie.common.repo

import com.xiaoxun.apie.common.utils.coroutine.singleSuspendCoroutine
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.resume

interface ExecuteResultDelegate {
    val disposables: CompositeDisposable

    /**
     * 用于执行 RxJava 的网络请求，有返回结果
     */
    suspend fun <T> executeResult(
        block: () -> Observable<T>
    ): Result<T> = singleSuspendCoroutine { continuation ->
        val disposable = block.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    continuation.resume(Result.success(result))
                },
                { error ->
                    continuation.resume(Result.failure(error))
                }
            )
        disposables.add(disposable)
    }

    /**
     * 用于执行 RxJava 的网络请求，没有返回结果
     */
    suspend fun executeNoResult(
        block: () -> Observable<Any>
    ): Result<Unit> = singleSuspendCoroutine { continuation ->
        val disposable = block.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    continuation.resume(Result.success(Unit))
                },
                { error ->
                    continuation.resume(Result.failure(error))
                }
            )
        disposables.add(disposable)
    }
}