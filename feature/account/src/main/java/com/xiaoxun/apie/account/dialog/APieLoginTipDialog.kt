package com.xiaoxun.apie.account.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.xiaoxun.apie.account.databinding.LayoutApieLoginTipDialogBinding
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.setDebouncingClickListener

class APieLoginTipDialog  (
    context: Context,
    private val titleRes: Int? = R.string.apie_login_cannot_accept_sms_tip
) : Dialog(context, R.style.ru_permission_dialog_style) {

    private lateinit var binding: LayoutApieLoginTipDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutApieLoginTipDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(true)

        binding.titleView.text = ContextCompat.getString(context, titleRes ?: R.string.apie_login_cannot_accept_sms_tip)

        binding.confirmBtn.setDebouncingClickListener {
            dismiss()
        }

        binding.cancelBtn.setDebouncingClickListener {
            dismiss()
        }
    }
}