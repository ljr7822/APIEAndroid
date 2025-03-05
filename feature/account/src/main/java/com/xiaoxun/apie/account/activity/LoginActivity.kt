package com.xiaoxun.apie.account.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.xiaoxun.apie.account.R
import com.xiaoxun.apie.account.databinding.LayoutApieLoginActivityBinding
import com.xiaoxun.apie.account.dialog.APieLoginTipDialog
import com.xiaoxun.apie.account.repo.AccountRepo
import com.xiaoxun.apie.account.repo.IAccountRepo
import com.xiaoxun.apie.account.viewmodel.AccountViewModel
import com.xiaoxun.apie.account.viewmodel.LoadingState
import com.xiaoxun.apie.account.viewmodel.LoginWayType
import com.xiaoxun.apie.account.viewmodel.SendSmsCodeStatus
import com.xiaoxun.apie.common.ACCOUNT_LOGIN_ACTIVITY_PATH
import com.xiaoxun.apie.common.HOME_INDEX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.ui.formatAsPhoneNumber
import com.xiaoxun.apie.common.ui.setEditTextMaxInput
import com.xiaoxun.apie.common.utils.KeyBoardUtils
import com.xiaoxun.apie.common.utils.PhoneNumberValidator
import com.xiaoxun.apie.common.utils.alphaHide
import com.xiaoxun.apie.common.utils.alphaShow
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.view_model.CommonLoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Route(path = ACCOUNT_LOGIN_ACTIVITY_PATH)
class LoginActivity : APieBaseBindingActivity<LayoutApieLoginActivityBinding>(
    LayoutApieLoginActivityBinding::inflate
) {

    companion object {
        private const val COUNTDOWN_TOTAL_MILLIS = 120 * 1000L
    }

    private val loadingDialog: APieLoadingDialog by lazy { APieLoadingDialog(this) }

    private val viewModel: AccountViewModel by lazy { AccountViewModel() }

    private val repo: IAccountRepo by lazy { AccountRepo(this, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeView()
    }

    override fun initializeView() {
        super.initializeView()
        initSwitchWayText()
        binding.phoneEdit.formatAsPhoneNumber()
        binding.passwordOrCodeEdit.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val length = s?.length?:0
                if (length > 0) {
                    viewModel.updateInputDoneStatus(true)
                } else {
                    viewModel.updateInputDoneStatus(false)
                }
            }
        })
        // 切换登录方式
        binding.switchWayLayout.setDebouncingClickListener {
            viewModel.switchLoginWay()
        }

        // 获取验证码
        binding.loginGetSmsCode.setDebouncingClickListener {
            val phoneNum = binding.phoneEdit.text.toString().replace(" ", "")
            if(PhoneNumberValidator.isValidPhoneNumber(phoneNum).not()) {
                APieToast.showDialog("手机号输入错误")
                return@setDebouncingClickListener
            }
            sendSmsCode(phoneNum)
        }

        // 登录
        binding.submitLayout.setDebouncingClickListener {
            KeyBoardUtils.hideKeyboard(this)
            val phoneNum = binding.phoneEdit.text.toString().replace(" ", "")
            val passwordOrCode = binding.passwordOrCodeEdit.text.toString().replace(" ", "")
            if(PhoneNumberValidator.isValidPhoneNumber(phoneNum).not()) {
                APieToast.showDialog("手机号输入错误")
                return@setDebouncingClickListener
            }
            if (viewModel.isLoginByPassword()) {
                loginByPassword(phoneNum, passwordOrCode)
            } else {
                loginBySmsCode(phoneNum, passwordOrCode)
            }
        }

        // 点击忘记密码或点击收不到短信
        binding.forgetTip.setDebouncingClickListener {
            if (viewModel.isSmsLoginWayType()) {
                val dialog = APieLoginTipDialog(this)
                dialog.show()
            }
            if (viewModel.isSmsLoginWayType().not()) {
                APieToast.showDialog("忘记密码仅能通过验证码登录")
                viewModel.switchLoginWay()
            }
        }
    }

    private fun initSwitchWayText() {
        changeLoginWayTip()
        changeSubTip()

        observe(viewModel.inputDoneStatus) {
            if (it) {
                binding.submitLayout.background = getDrawable(R.drawable.apie_login_submit_btn_bg)
                binding.submitLayout.isEnabled = true
            } else {
                binding.submitLayout.background = getDrawable(R.drawable.apie_login_submit_btn_disable_bg)
                binding.submitLayout.isEnabled = false
            }
        }

        observe(viewModel.currentLoginWayType) {
            if (it == null) {
                return@observe
            }
            changeLoginWayTip()
            changeSubTip()
            switchLoginWay(it)
        }

        observe(viewModel.loadingState) { pair ->
            when (pair.first) {
                LoadingState.Loading -> {
                    binding.submitTip.hide()
                    binding.submitLoading.show()
                }
                LoadingState.Success -> {
                    APieToast.showDialog(pair.second.message)
                    binding.submitTip.show()
                    binding.submitLoading.hide()
                    ARouter.getInstance().build(HOME_INDEX_ACTIVITY_PATH).navigation();
                    // 登录成功后初始化下token
                    lifecycleScope.launch {
                        repo.getSTSToken()
                    }
                }
                LoadingState.Failed -> {
                    APieToast.showDialog(pair.second.message)
                    binding.submitTip.show()
                    binding.submitLoading.hide()
                }
                else -> {}
            }
        }

        observe(viewModel.sendSmsCodeStatus) {
            when(it) {
                SendSmsCodeStatus.SendSuccess -> {
                    APieToast.showDialog("验证码发送成功")
                    startCountdown()
                }
                SendSmsCodeStatus.SendFailed -> {
                    APieToast.showDialog("验证码发送失败")
                }
                else -> {}
            }
        }

        observe(viewModel.commonLoginStatus) {
            when(it) {
                CommonLoadingState.START -> {
                    loadingDialog.show()
                }
                CommonLoadingState.SUCCESS -> {
                    loadingDialog.dismiss()
                }
                CommonLoadingState.FAILED -> {
                    loadingDialog.dismiss()
                }
                else -> {}
            }
        }
    }

    private fun changeLoginWayTip() {
        binding.switchWayText.text =
            if (viewModel.isSmsLoginWayType()) {
                getString(com.xiaoxun.apie.common.R.string.apie_login_switch_password_tip)
            } else {
                getString(com.xiaoxun.apie.common.R.string.apie_login_switch_sms_code_tip)
            }
        binding.forgetTip.text =
            if (viewModel.isSmsLoginWayType()) {
                getString(com.xiaoxun.apie.common.R.string.apie_login_cannot_accept_sms_tip)
            } else {
                getString(com.xiaoxun.apie.common.R.string.apie_login_forget_password_tip)
            }
    }

    private fun changeSubTip() {
        binding.loginSubTitle.text = if (viewModel.isSmsLoginWayType()) {
            getString(com.xiaoxun.apie.common.R.string.apie_login_sms_sub_title)
        } else {
            getString(com.xiaoxun.apie.common.R.string.apie_login_password_sub_title)
        }
    }

    private fun startCountdown() {
        object : CountDownTimer(COUNTDOWN_TOTAL_MILLIS, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                binding.loginGetSmsCode.text =
                    getString(com.xiaoxun.apie.common.R.string.login_reget_sms_code, secondsLeft)
                binding.loginGetSmsCode.isEnabled = false
            }

            override fun onFinish() {
                binding.loginGetSmsCode.text =
                    getString(com.xiaoxun.apie.common.R.string.login_get_sms_code)
                binding.loginGetSmsCode.isEnabled = true
            }
        }.start()
    }

    private fun switchLoginWay(loginWayType: LoginWayType) {
        binding.passwordOrCodeEdit.setText("")
        if (loginWayType == LoginWayType.PASSWORD) {
            binding.passwordOrCodeIcon.setImageResource(R.drawable.apie_login_password_icon)
            binding.passwordOrCodeEdit.hint =
                getString(com.xiaoxun.apie.common.R.string.login_input_password_hint)
            binding.passwordOrCodeEdit.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.passwordOrCodeEdit.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.passwordOrCodeEdit.setEditTextMaxInput(APieConfig.DEFAULT_PASSWORD_SIZE)
            binding.loginGetSmsCode.alphaHide(150)
        } else {
            binding.passwordOrCodeIcon.setImageResource(R.drawable.apie_login_code_icon)
            binding.passwordOrCodeEdit.hint =
                getString(com.xiaoxun.apie.common.R.string.login_input_sms_code_hint)
            binding.passwordOrCodeEdit.inputType = InputType.TYPE_CLASS_NUMBER
            binding.passwordOrCodeEdit.setEditTextMaxInput(APieConfig.DEFAULT_SMS_CODE_SIZE)
            binding.loginGetSmsCode.alphaShow(150)
        }
    }

    /**
     * 验证码登录
     */
    private fun loginBySmsCode(phoneNum: String, smsCode: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            repo.startLoginBySmsCode(phoneNum, smsCode)
        }
    }

    /**
     * 密码登录
     */
    private fun loginByPassword(phoneNum: String, password: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            repo.startLoginByPassword(phoneNum, password)
        }
    }

    /**
     * 发送验证码
     */
    private fun sendSmsCode(phoneNum: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            repo.getSmsCode(phoneNum)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        repo.onCleared()
    }
}