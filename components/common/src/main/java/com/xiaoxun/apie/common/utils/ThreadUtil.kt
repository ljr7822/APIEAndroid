package com.xiaoxun.apie.common.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadUtil {
    // 主线程的Handler
    private val mainHandler = Handler(Looper.getMainLooper())
    // 计数
    private var count = 0
    // 使用线程池
    private val executor: ExecutorService = ThreadPoolExecutor(
        2,
        4,
        5000L,
        TimeUnit.MILLISECONDS,
        LinkedBlockingQueue(),
        ThreadFactory { runnable ->
            Thread(runnable, "Background Runner ${count++}")
        }
    )

    /**
     * 判断当前是否在主线程
     *
     * @return true-是；false-不是
     */
    fun isInMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    /**
     * 在主线程运行
     */
    fun runOnMainThread(runner: Runnable?) {
        if (runner == null) return
        if (isInMainThread()) {
            runner.run()
        } else {
            mainHandler.post(runner)
        }
    }

    /**
     * 在主线程延迟运行
     */
    fun runOnMainThreadDelay(runnable: Runnable?, delayMs: Long) {
        if (runnable == null) return
        mainHandler.postDelayed(runnable, delayMs)
    }

    /**
     * 移除 Runnable
     */
    fun removeRunnable(runnable: Runnable?) {
        if (runnable == null) return
        mainHandler.removeCallbacks(runnable)
    }

    /**
     * 在子线程运行
     */
    fun runOnChildThread(runner: Runnable?) {
        if (runner == null) return
        executor.submit(runner)
    }

    /**
     * 在子线程延迟运行
     */
    fun runOnChildThreadDelay(runner: Runnable?, delayMs: Long) {
        if (runner == null) return
        mainHandler.postDelayed({
            executor.submit(runner)
        }, delayMs)
    }

    /**
     * 在后台线程上运行
     */
    fun runInBackground(runnable: Runnable?) {
        if (runnable == null) return
        executor.submit(runnable)
    }
}