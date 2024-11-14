package com.xiaoxun.apie.data_loader.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 线程工具类
 */
object ThreadUtils {
    // Main thread Handler
    private val mainHandler = Handler(Looper.getMainLooper())

    // Counter for naming threads
    private var count = 0

    // Thread pool
    private val executor: ExecutorService = ThreadPoolExecutor(
        2,
        4,
        5000,
        TimeUnit.MICROSECONDS,
        LinkedBlockingQueue(),
        ThreadFactory { Runnable ->
            Thread(Runnable, "Background Runner ${count++}")
        }
    )

    fun init() {
        // Initialization if needed
    }

    /**
     * Checks if currently on the main thread
     * @return true if on main thread; false otherwise
     */
    fun isInMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    /**
     * Runs on main thread
     */
    fun runOnMainThread(runner: Runnable?) {
        runner?.let {
            if (isInMainThread()) {
                it.run()
            } else {
                mainHandler.post(it)
            }
        }
    }

    /**
     * Runs on main thread with delay
     */
    fun runOnMainThreadDelay(runnable: Runnable?, delayMs: Long) {
        runnable?.let {
            mainHandler.postDelayed(it, delayMs)
        }
    }

    /**
     * Removes a Runnable
     */
    fun removeRunnable(runnable: Runnable?) {
        runnable?.let {
            mainHandler.removeCallbacks(it)
        }
    }

    /**
     * Runs on child thread
     */
    fun runOnChildThread(runner: Runnable?) {
        runner?.let {
            executor.submit(it)
        }
    }

    /**
     * Runs on child thread with delay
     */
    fun runOnChildThreadDelay(runner: Runnable?, delayMs: Long) {
        runner?.let {
            mainHandler.postDelayed({
                executor.submit(it)
            }, delayMs)
        }
    }

    /**
     * Runs in background thread
     */
    fun runInBackground(runnable: Runnable?) {
        runnable?.let {
            executor.submit(it)
        }
    }
}
