package com.xiaoxun.apie.common.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * 使用 ViewBinding 的 Activity 基类
 */
abstract class APieBaseBindingActivity<VB : ViewBinding>(
    private val inflate: (LayoutInflater) -> VB
) : AppCompatActivity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
    }

    /**
     * 子类可重写以进行视图初始化。
     */
    protected open fun initializeView() {}
}