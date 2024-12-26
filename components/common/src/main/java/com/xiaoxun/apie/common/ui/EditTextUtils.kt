package com.xiaoxun.apie.common.ui

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import com.xiaoxun.apie.common.config.APieConfig.PHONE_NUMBER_MAX_LENGTH

/**
 * 格式化手机号为 1xx xxxx xxxx 格式，并限制最多输入 11 位
 */
fun EditText.formatAsPhoneNumber() {
    setEditTextMaxInput(PHONE_NUMBER_MAX_LENGTH + 2)
    val editText = this
    editText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val phone = s.toString()
            var value = phone.replace(" ", "")
            if (value.length > 3) {
                value = value.substring(0, 3) + " " + value.substring(3)
            }
            if (value.length > 8) {
                value = value.substring(0, 8) + " " + value.substring(8)
            }

            editText.removeTextChangedListener(this)
            editText.setText(value)
            editText.addTextChangedListener(this)
            editText.setSelection(editText.text.length)
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}

fun EditText.setEditTextMaxInput(maxLength: Int) {
    val filter = InputFilter.LengthFilter(maxLength)
    this.filters = arrayOf(filter)
}