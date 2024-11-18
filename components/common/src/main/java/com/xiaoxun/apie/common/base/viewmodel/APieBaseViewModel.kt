package com.xiaoxun.apie.common.base.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.xiaoxun.apie.common.utils.coroutine.singleSuspendCoroutine
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.resume

/**
 * 基类ViewModel
 */
open class APieBaseViewModel: ViewModel(), DefaultLifecycleObserver {

    private val disposables = CompositeDisposable()

    /**
     * 用于执行 RxJava 的网络请求并返回结果
     */
    protected suspend fun <T> executeResult(
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
    protected suspend fun executeNoResult(
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

    /**
     * 在生命周期结束时清理订阅
     */
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    /**
     * 当 ViewModel 关联的 LifecycleOwner 销毁时清理订阅
     */
    override fun onDestroy(owner: LifecycleOwner) {
        disposables.clear()
    }
}