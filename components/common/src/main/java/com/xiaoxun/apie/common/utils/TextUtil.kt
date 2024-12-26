package com.xiaoxun.apie.common.utils

object TextUtil {
    /**
     * 去除空格后的字符串长度
     */
    @JvmStatic
    fun getNoSpaceLength(cs: CharSequence?): Int {
        var length = 0
        if (cs == null || cs.isEmpty()) return length
        for (i in 0 until cs.length) {
            val c = cs[i]
            if (c != ' ') {
                length++
            }
        }
        return length
    }
}