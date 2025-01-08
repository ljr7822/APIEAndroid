package com.xiaoxun.apie.home_page.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreatePlanGroupFragmentBinding

class APieCreateGroupDialog (
    context: Context,
    private val titleRes: Int?,
    private val onConfirm: (String) -> Unit,
    private val onCancel: (() -> Unit)? = null
) : Dialog(context, R.style.ru_permission_dialog_style) {

    private lateinit var binding: LayoutApieCreatePlanGroupFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutApieCreatePlanGroupFragmentBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(true)

        binding.titleView.text = ContextCompat.getString(context, titleRes ?: R.string.apie_create_plan_group_title)

        binding.confirmBtn.setDebouncingClickListener {
            onConfirm.invoke(binding.nameEdit.text.toString())
            dismiss()
        }

        binding.cancelBtn.setDebouncingClickListener {
            onCancel?.invoke()
            dismiss()
        }
    }
}