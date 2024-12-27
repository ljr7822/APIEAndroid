package com.xiaoxun.apie.common.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ImmersionBar

/**
 * 使用 ViewBinding 的 Activity 基类
 */
abstract class APieBaseBindingActivity<VB : ViewBinding>(
    private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity() {

    /**
     * 是否开启沉浸式
     */
    private var enableImmersive: Boolean = true

    lateinit var binding: VB

    private val observers: MutableList<Pair<LiveData<*>, Observer<*>>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        initWindowsStyle()
    }

    fun <T> observe(imageData: LiveData<T>, observer: Observer<T>) {
        imageData.observe(this, observer)
        observers.add(Pair(imageData, observer))
    }

    override fun onDestroy() {
        super.onDestroy()
        observers.forEach {
            it.first.removeObserver(it.second as Observer<in Any>)
        }
        observers.clear()
    }

    fun setEnableImmersive(enableImmersive: Boolean) {
        this.enableImmersive = enableImmersive
    }

    /**
     * 子类可重写以进行视图初始化。
     */
    protected open fun initializeView() {}

    private fun initWindowsStyle() {
        if (enableImmersive) {
            ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init()
        }
    }
}