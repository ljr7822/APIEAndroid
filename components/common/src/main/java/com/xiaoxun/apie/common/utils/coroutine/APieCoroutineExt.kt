package com.xiaoxun.apie.common.utils.coroutine

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine

/**
 * 使用 [singleSuspendCoroutine] 可将 RxJava 异步流的结果转换为 Kotlin 的挂起函数返回值。
 * 例如：
 * suspend fun login(account: String, password: String, pushId: String): Result<BaseResponse<AccountModel>> {
 *         val loginRequestBody = LoginRequestBody(account, password, pushId)
 *         return singleSuspendCoroutine { continuation ->
 *             val disposable = DataLoaderManager.instance
 *                 .login(LoginRequest(loginRequestBody), CacheStrategy.FORCE_NET)
 *                 .subscribeOn(Schedulers.io())
 *                 .observeOn(AndroidSchedulers.mainThread())
 *                 .subscribe({
 *                     APieLog.d("AccountLoginViewModel", "login success: $it")
 *                     continuation.resume(Result.success(it))
 *                 },{
 *                     APieLog.d("AccountLoginViewModel", "login error: $it")
 *                     continuation.resume(Result.failure(it))
 *                 })
 *             disposables.add(disposable)
 *         }
 *     }
 */
suspend inline fun <T> singleSuspendCoroutine(crossinline block: (Continuation<T>) -> Unit): T {
    return suspendCoroutine {
        block(SafeContinuation(it))
    }
}

/**
 * 确保挂起函数的 resume 调用是线程安全的
 */
class SafeContinuation<T>(private val target: Continuation<T>) : Continuation<T> by target {
    private var resumed = AtomicBoolean(false)
    override fun resumeWith(result: Result<T>) {
        if (resumed.compareAndSet(false, true)) {
            target.resumeWith(result)
        }
    }
}