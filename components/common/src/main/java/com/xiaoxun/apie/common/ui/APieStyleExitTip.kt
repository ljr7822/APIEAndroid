package com.xiaoxun.apie.common.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.databinding.ApieCommonStyleExitTipBinding
import com.xiaoxun.apie.common.utils.setDebouncingClickListener

class APieStyleExitTip(
    val activity: AppCompatActivity,
    val key: String,
    val actionCallback: () -> Unit,
    val cancelCallback: () -> Unit = {}
) : Dialog(activity, R.style.ru_permission_dialog_style) {

    private lateinit var binding: ApieCommonStyleExitTipBinding

    companion object {
        const val APIE_RESECT_PLAN_COUNT_KEY = "APIE_RESECT_PLAN_COUNT_KEY"

        fun show(
            activity: AppCompatActivity,
            key: String,
            actionCallback: () -> Unit,
            cancelCallback: () -> Unit = {}
        ) {
            if (key != APIE_RESECT_PLAN_COUNT_KEY) return
            if (Looper.getMainLooper() != Looper.myLooper()) {
                Handler(Looper.getMainLooper()).post {
                    APieStyleExitTip(activity, key, actionCallback, cancelCallback).show()
                }
            } else {
                APieStyleExitTip(activity, key, actionCallback, cancelCallback).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApieCommonStyleExitTipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        binding.styleHintCancelBtn.setDebouncingClickListener {
            dismiss()
        }
        val title = when (key) {
            APIE_RESECT_PLAN_COUNT_KEY -> R.string.apie_plan_item_reset_count_tip
            else -> R.string.app_name
        }

        // 如果key为TIP_IMAGE_UPLOAD_KEY，则不显示"下次不再提示"文字
        if (key == APIE_RESECT_PLAN_COUNT_KEY) {
            binding.styleSelectBtn.visibility = View.GONE
            binding.styleNoHint.visibility = View.GONE
        } else {
            binding.noHintArea.setOnClickListener { setSelectClick(it) }
        }
        binding.styleTitle.setText(title)
        binding.styleHintCancelSure.setOnClickListener {
            dismiss()
            actionCallback.invoke()
        }
    }

    private fun setSelectClick(view: View) {
        binding.styleSelectBtn.isSelected = binding.styleSelectBtn.isSelected.not()
    }
}