package com.xiaoxun.apie.home_page.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.ui.setEditTextMaxInput
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.KeyBoardUtils
import com.xiaoxun.apie.common.utils.rsa.PasswordEncryptorUtils
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.home_page.bean.ChangePasswordInfo
import com.xiaoxun.apie.home_page.databinding.LayoutApieChangePasswordTipDialogBinding

class APieChangePasswordDialog(
    context: Context,
    private val onConfirm: (ChangePasswordInfo) -> Unit,
    private val titleRes: Int? = R.string.apie_login_change_password_tip
) : Dialog(context, R.style.ru_permission_dialog_style) {

    companion object {
        private const val TAG = "APieChangePasswordDialog"
    }

    private lateinit var binding: LayoutApieChangePasswordTipDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LayoutApieChangePasswordTipDialogBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(true)

        binding.titleView.text =
            ContextCompat.getString(context, titleRes ?: R.string.apie_login_change_password_tip)

        binding.newPasswordEdit.setEditTextMaxInput(APieConfig.DEFAULT_PASSWORD_SIZE)

        binding.cancelBtn.setDebouncingClickListener {
            dismiss()
        }

        binding.confirmBtn.setDebouncingClickListener {
            KeyBoardUtils.hideKeyboardFromDialog(this)

            if (checkPasswordIsEmpty()) {
                APieToast.showDialog("密码不能为空")
                return@setDebouncingClickListener
            }

            if (checkPasswordConsistent()) {
                APieToast.showDialog("新密码不能和原密码一致")
                return@setDebouncingClickListener
            }

            if (checkPasswordLength()) {
                APieToast.showDialog("密码长度不能小于${APieConfig.DEFAULT_PASSWORD_SIZE}位")
                return@setDebouncingClickListener
            }

            val newPassword = binding.newPasswordEdit.text.toString()
            val oldPassword = binding.oldPasswordInputEdit.text.toString()
            val publicKey = AccountMMKVRepository.publicKey

            if (publicKey.isNullOrEmpty()) {
                APieToast.showDialog("操作失败，请重试")
                APieLog.e(TAG, "获取公钥失败")
                return@setDebouncingClickListener
            }

            val encodeOldPassword = PasswordEncryptorUtils.encrypt(password = oldPassword, publicKeyString = publicKey)
            val encodeNewPassword = PasswordEncryptorUtils.encrypt(password = newPassword, publicKeyString = publicKey)
            APieLog.d(TAG, "密码加密成功：encodeOldPassword=$encodeOldPassword, encodeNewPassword=$encodeNewPassword")

            onConfirm.invoke(ChangePasswordInfo(encodeOldPassword, encodeNewPassword))
            dismiss()
        }
    }

    /**
     * 检查是否输入
     */
    private fun checkPasswordIsEmpty(): Boolean {
        val newPassword = binding.newPasswordEdit.text.toString()
        val oldPassword = binding.oldPasswordInputEdit.text.toString()
        return newPassword.trim().isEmpty() ||  oldPassword.trim().isEmpty()
    }

    /**
     * 检查原始密码和新密码是否一致
     */
    private fun checkPasswordConsistent(): Boolean {
        val newPassword = binding.newPasswordEdit.text.toString()
        val oldPassword = binding.oldPasswordInputEdit.text.toString()
        return newPassword == oldPassword
    }

    /**
     * 检查新密码长度
     */
    private fun checkPasswordLength(): Boolean {
        val newPassword = binding.newPasswordEdit.text.toString()
        return newPassword.length < APieConfig.DEFAULT_PASSWORD_SIZE
    }
}