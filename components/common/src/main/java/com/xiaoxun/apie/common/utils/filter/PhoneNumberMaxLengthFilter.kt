package com.xiaoxun.apie.common.utils.filter

import android.text.InputFilter
import android.text.Spanned
import com.xiaoxun.apie.common.utils.TextUtil

class PhoneNumberMaxLengthFilter(private val max:Int) : InputFilter {

    override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        if (TextUtil.getNoSpaceLength(source) + TextUtil.getNoSpaceLength(dest) <= max) {
            return null
        }
        return ""
    }
}