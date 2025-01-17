package com.xiaoxun.apie.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * 基础的页面Fragment
 */
abstract class APieBaseBindingFragment<VB : ViewBinding>(
    private val inflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseLogFragment() {

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    // 提供一个强类型的 AppCompatActivity 属性
    protected val hostActivity: AppCompatActivity
        get() = requireActivity() as AppCompatActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflater(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTopBarView()
    }

    open fun initTopBarView(){}
}