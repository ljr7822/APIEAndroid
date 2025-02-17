package com.xiaoxun.apie.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.dp

abstract class APieBaseBottomSheetDialogFragment<VB : ViewBinding>(
    private val inflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    // 展开时初始显示的高度
    protected var peekHeight: Int? = null
    // 最大高度
    protected var layoutHeight: Int? = null

    protected var enableCancel = false

    protected var isHideable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = enableCancel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialogStyle()
    }

    private fun initDialogStyle() {
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
            // 设置背景资源
            setBackgroundResource(com.xiaoxun.apie.common.R.drawable.apie_create_plan_fragment_bg)

            // 配置 BottomSheetBehavior
            val behavior = BottomSheetBehavior.from(this)
            val screenHeight = UIUtils.getScreenRealHeight(requireContext())
            behavior.peekHeight = peekHeight ?: (screenHeight - 100.dp)
            layoutParams.height = layoutHeight ?: (screenHeight - 100.dp)
            behavior.isHideable = isHideable
            behavior.skipCollapsed = isHideable
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    override fun getTheme(): Int {
        return com.xiaoxun.apie.common.R.style.APieBottomSheetAnimation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}