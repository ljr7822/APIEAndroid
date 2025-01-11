package com.xiaoxun.apie.account.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RelativeLayout
import com.xiaoxun.apie.account.R
import com.xiaoxun.apie.account.databinding.LoginViewPhoneEdittextV3Binding
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.LoginUtils
import com.xiaoxun.apie.common.utils.RegisterCountryPhoneCode
import com.xiaoxun.apie.common.utils.DateTimeUtils
import com.xiaoxun.apie.common.utils.TextUtil
import com.xiaoxun.apie.common.utils.filter.PhoneNumberMaxLengthFilter
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setAccessClassNameDelegate
import com.xiaoxun.apie.common.utils.showIf

/**
 * Created by susion on 17/8/24.
 * 输入手机号的 带 EditText 的 View
 */
class PhoneNumberEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : RelativeLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "PhoneNumberEditText"
    }

    private val binding: LoginViewPhoneEdittextV3Binding =
        LoginViewPhoneEdittextV3Binding.inflate(LayoutInflater.from(context), this, true)

    private var mCountryPhoneCode = RegisterCountryPhoneCode.CHINA_PHONE_CODE
    var listener: InputPhoneNumberListener? = null
    private var mHasMask = false

    private val mTextWatchListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (mHasMask) {
                binding.mPhoneNumberEditText.setText("")
                binding.mCancelInputImageView.showIf(false)
                binding.mPhoneNumberEditText.requestFocus()
                return
            }
            binding.mCancelInputImageView.showIf(s.toString().isNotEmpty())
        }

        private var beforeText: String = DateTimeUtils.EMPTY_STRING

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            s?.apply {
                mHasMask = contains('*')
            }
            beforeText = s.toString()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null && beforeText.length < s.length) {
                listener?.onStartInput()
            }
            val pureNumber = s.toString().replace(" ", "").trim()
            val newNumber = LoginUtils.processPhoneNumberForDiffCountry(
                mCountryPhoneCode,
                pureNumber, start, count < before
            )

            var newSelectIndex = start

            if (count - before > 0) { //增加内容,  如果额外插入了空格， 那么新的selection 应移动两步
                newSelectIndex =
                    if (newNumber.length - s.toString().length == 1) start + 2 else start + 1
            }

            setPhoneNumber(newNumber, newSelectIndex)
        }
    }

    private fun checkIfFinish(newContent: String) {
        when (mCountryPhoneCode) {
            RegisterCountryPhoneCode.CHINA_PHONE_CODE -> {
                if (TextUtil.getNoSpaceLength(newContent) >= 11) listener?.onFinishInputPhoneNumber(
                    true
                ) else listener?.onFinishInputPhoneNumber(false)

            }

            RegisterCountryPhoneCode.AMERICA_PHONE_CODE -> {
                if (TextUtil.getNoSpaceLength(newContent) >= 10) listener?.onFinishInputPhoneNumber(
                    true
                ) else listener?.onFinishInputPhoneNumber(false)
            }

            else -> {
                if (newContent.isNotEmpty()) listener?.onFinishInputPhoneNumber(
                    true
                ) else listener?.onFinishInputPhoneNumber(false)
            }
        }
    }


    init {
        initView()
    }

    private fun initView() {
        LayoutInflater.from(context).inflate(R.layout.login_view_phone_edittext_v3, this)
        try {
            binding.mAreaNumberTextView.setOnClickListener {
                listener?.onSelectCountryNumberViewClick()
            }

            binding.mCancelInputImageView.setOnClickListener {
                binding.mPhoneNumberEditText.setText("")
                binding.mPhoneNumberEditText.requestFocus()
            }

            binding.mPhoneNumberEditText.addTextChangedListener(mTextWatchListener)
            setMaxPhoneNumberLength()
        } catch (e: Exception) {
            APieLog.e("PhoneNumberEditText", e.toString())
        }

        initAccessibility()
    }

    private fun initAccessibility() {
        binding.mAreaNumberTextView.contentDescription =
            resources.getString(com.xiaoxun.apie.common.R.string.login_phone_zone_code) + binding.mAreaNumberTextView.text
        binding.mCancelInputImageView.setAccessClassNameDelegate(Button::class.java.name)
    }

    /**
     * 手机号输入Listener
     */
    interface InputPhoneNumberListener {
        /**区号按钮点击监听*/
        fun onSelectCountryNumberViewClick()

        /**监听输入状态是否足位，目前只针对 中国 和 美国 */
        fun onFinishInputPhoneNumber(isFinish: Boolean)

        /**监听从头开始输入 */
        fun onStartInput()
    }

    /**
     * 设置手机号
     *
     * @param newNumber
     * @param selectIndex
     */
    fun setPhoneNumber(newNumber: String, selectIndex: Int = -1) {
        binding.mPhoneNumberEditText.apply {
            removeTextChangedListener(mTextWatchListener)
            setText(newNumber)
            // 华为mate pad大概率会崩溃，其他机型没有，先catch处理
            try {
                setSelection(
                    if (selectIndex == -1) {
                        binding.mPhoneNumberEditText.text.length
                    } else {
                        selectIndex
                    }
                )
            } catch (e: IndexOutOfBoundsException) {
                APieLog.e(TAG, e.toString())
            }
            addTextChangedListener(mTextWatchListener)
        }
        binding.mCancelInputImageView.showIf(newNumber.isNotEmpty())
        checkIfFinish(newNumber)
    }


    /**
     * 设置手机号区号
     *
     * @param phoneCode
     */
    fun setCountryPhoneCode(phoneCode: String?) {
        mCountryPhoneCode = phoneCode ?: ""
        binding.mAreaNumberTextView.contentDescription =
            resources.getString(com.xiaoxun.apie.common.R.string.login_phone_zone_code) + mCountryPhoneCode
        binding.mAreaNumberTextView.text = context.getString(
            com.xiaoxun.apie.common.R.string.login_phone_code_prefix,
            mCountryPhoneCode
        )
        binding.mPhoneNumberEditText.setText("")
        setMaxPhoneNumberLength()
    }

    private fun setMaxPhoneNumberLength() {
        when (mCountryPhoneCode) {
            RegisterCountryPhoneCode.CHINA_PHONE_CODE -> {
                binding.mPhoneNumberEditText.filters = arrayOf<InputFilter>(
                    PhoneNumberMaxLengthFilter(11)
                )
            }

            RegisterCountryPhoneCode.AMERICA_PHONE_CODE -> {
                binding.mPhoneNumberEditText.filters =
                    arrayOf<InputFilter>(PhoneNumberMaxLengthFilter(10))
            }

            else -> {
                binding.mPhoneNumberEditText.filters = arrayOf<InputFilter>(
                    PhoneNumberMaxLengthFilter(RegisterCountryPhoneCode.MAX_COUNT)
                )
            }
        }
    }

    /**
     * 获取手机号
     *
     * @return
     */
    fun getPhoneNumber(): String {
        return binding.mPhoneNumberEditText.text.toString().replace(" ", "")
    }

    /**
     * 获取手机号区号
     *
     * @return
     */
    fun getPhoneCountryCode(): String {
        return mCountryPhoneCode
    }

    /**
     * disable
     */
    fun disable() {
        binding.mCancelInputImageView.hide()
        binding.mAreaNumberTextView.setCompoundDrawables(null, null, null, null)
        binding.mPhoneNumberEditText.setTextColor(context.getColor(com.xiaoxun.apie.common.R.color.apieTheme_colorGrayLevel3))
        binding.mPhoneNumberEditText.isEnabled = false
    }
}