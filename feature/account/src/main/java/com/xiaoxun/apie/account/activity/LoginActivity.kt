package com.xiaoxun.apie.account.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputType
import com.gyf.immersionbar.ImmersionBar
import com.xiaoxun.apie.account.R
import com.xiaoxun.apie.account.databinding.LayoutApieLoginActivityBinding
import com.xiaoxun.apie.account.repo.AccountRepo
import com.xiaoxun.apie.account.repo.IAccountRepo
import com.xiaoxun.apie.account.viewmodel.AccountViewModel
import com.xiaoxun.apie.account.viewmodel.LoadingState
import com.xiaoxun.apie.account.viewmodel.LoginWayType
import com.xiaoxun.apie.account.viewmodel.SendSmsCodeStatus
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.ui.formatAsPhoneNumber
import com.xiaoxun.apie.common.ui.setEditTextMaxInput
import com.xiaoxun.apie.common.utils.alphaHide
import com.xiaoxun.apie.common.utils.alphaShow
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common.utils.toast.APieToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : APieBaseBindingActivity<LayoutApieLoginActivityBinding>(
    LayoutApieLoginActivityBinding::inflate
) {

    companion object {
        private const val COUNTDOWN_TOTAL_MILLIS = 120 * 1000L
    }

    private val viewModel: AccountViewModel by lazy { AccountViewModel() }

    private val repo: IAccountRepo by lazy { AccountRepo(this, viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowsStyle()
        initializeView()
    }

    private fun initWindowsStyle() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
    }

    override fun initializeView() {
        super.initializeView()
        initSwitchWayText()
        binding.phoneEdit.formatAsPhoneNumber()
        // 切换登录方式
        binding.switchWayLayout.setDebouncingClickListener {
            viewModel.switchLoginWay()
        }

        // 获取验证码
        binding.loginGetSmsCode.setDebouncingClickListener {
            binding.phoneEdit.text.toString().let {
                if (it.isEmpty()) {
                    APieToast.showDialog("手机号输入错误")
                    return@let
                }
                sendSmsCode(it)
            }
        }

        // 登录
        binding.submitLayout.setDebouncingClickListener {
            val phoneNum = binding.phoneEdit.text.toString()
            val passwordOrCode = binding.passwordOrCodeEdit.text.toString()
            if (viewModel.isLoginByPassword()) {
                loginByPassword(phoneNum, passwordOrCode)
            } else {
                loginBySmsCode(phoneNum, passwordOrCode)
            }
        }
    }

    /**
     * 验证码登录
     */
    private fun loginBySmsCode(phoneNum: String, smsCode: String) {
        GlobalScope.launch(Dispatchers.Main) {
            repo.startLoginBySmsCode(phoneNum, smsCode)
        }
    }

    /**
     * 密码登录
     */
    private fun loginByPassword(phoneNum: String, password: String) {
        GlobalScope.launch(Dispatchers.Main) {
            repo.startLoginByPassword(phoneNum, password)
        }
    }

    /**
     * 发送验证码
     */
    private fun sendSmsCode(phoneNum: String, userId: String = "99678322425856") {
        GlobalScope.launch(Dispatchers.Main) {
            repo.getSmsCode(phoneNum, userId)
        }
    }

    private fun initSwitchWayText() {
        binding.switchWayText.text =
            if (viewModel.currentLoginWayType.value == LoginWayType.SMS_CODE) {
                getString(com.xiaoxun.apie.common.R.string.apie_login_switch_password_tip)
            } else {
                getString(com.xiaoxun.apie.common.R.string.apie_login_switch_sms_code_tip)
            }

        observe(viewModel.currentLoginWayType) {
            if (it == null) {
                return@observe
            }

            binding.switchWayText.text = if (it == LoginWayType.SMS_CODE) {
                getString(com.xiaoxun.apie.common.R.string.apie_login_switch_password_tip)
            } else {
                getString(com.xiaoxun.apie.common.R.string.apie_login_switch_sms_code_tip)
            }
            switchLoginWay(it)
        }

        observe(viewModel.loadingState) {
            when (it) {
                LoadingState.Loading -> {
                    binding.submitTip.hide()
                    binding.submitLoading.show()
                }
                LoadingState.Success -> {
                    APieToast.showDialog("登录成功")
                    binding.submitTip.show()
                    binding.submitLoading.hide()
                }
                LoadingState.Failed -> {
                    APieToast.showDialog("登录失败")
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
        if (loginWayType == LoginWayType.PASSWORD) {
            binding.passwordOrCodeIcon.setImageResource(R.drawable.apie_login_password_icon)
            binding.passwordOrCodeEdit.hint =
                getString(com.xiaoxun.apie.common.R.string.login_input_password_hint)
            binding.passwordOrCodeEdit.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.loginGetSmsCode.alphaHide(200)
        } else {
            binding.passwordOrCodeIcon.setImageResource(R.drawable.apie_login_code_icon)
            binding.passwordOrCodeEdit.hint =
                getString(com.xiaoxun.apie.common.R.string.login_input_sms_code_hint)
            binding.passwordOrCodeEdit.inputType = InputType.TYPE_CLASS_NUMBER
            binding.passwordOrCodeEdit.setEditTextMaxInput(4)
            binding.loginGetSmsCode.alphaShow(200)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        repo.onCleared()
    }
}