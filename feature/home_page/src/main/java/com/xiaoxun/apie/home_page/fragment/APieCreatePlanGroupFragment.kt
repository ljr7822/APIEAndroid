package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreatePlanGroupFragmentBinding

class APieCreatePlanGroupFragment :
    APieBaseBottomSheetDialogFragment<LayoutApieCreatePlanGroupFragmentBinding>(
        LayoutApieCreatePlanGroupFragmentBinding::inflate
    ) {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableCancel = true
        super.onCreate(savedInstanceState)
        val screenHeight = UIUtils.getScreenRealHeight(requireContext())
        peekHeight = screenHeight / 3
        layoutHeight = screenHeight / 3
    }
}