package com.xiaoxun.apie.common.utils

object LoginUtils {
    /**
     * 获取输入框中的手机号
     *
     * @param countryPhoneCode 国家码
     * @param phoneNumber 手机号
     * @param operateIndex 当前的Index
     * @param isDelete 是否是删除
     */
    fun processPhoneNumberForDiffCountry(countryPhoneCode: String, phoneNumber: String,
                                         operateIndex: Int = 0, isDelete: Boolean = false): String {
        return when (countryPhoneCode) {
            RegisterCountryPhoneCode.CHINA_PHONE_CODE -> {
                processPhoneNumberForChina(phoneNumber, operateIndex, isDelete)
            }
            RegisterCountryPhoneCode.AMERICA_PHONE_CODE -> {
                processPhoneNumberForAmerica(phoneNumber, operateIndex, isDelete)
            }
            else -> return phoneNumber
        }
    }

    //手机号 分段格式化 for 中国
    private fun processPhoneNumberForChina(phoneNumber: String, operateIndex: Int, isDelete: Boolean): String {
        val stringBuilder = StringBuilder(phoneNumber)

        if (!isDelete) {
            if (phoneNumber.length >= 3) {
                stringBuilder.insert(3, " ")
            }

            if (phoneNumber.length >= 7) {
                stringBuilder.insert(8, " ")
            }
        }

        if (isDelete) {
            if (phoneNumber.length >= 3 && operateIndex != 3) {
                stringBuilder.insert(3, " ")
            }

            if (phoneNumber.length >= 7 && operateIndex != 8 && operateIndex != 3) {
                stringBuilder.insert(8, " ")
            }
        }

        return stringBuilder.toString()
    }

    //手机号 分段格式化 for 美国
    private fun processPhoneNumberForAmerica(phoneNumber: String, operateIndex: Int, isDelete: Boolean): String {
        val stringBuilder = StringBuilder(phoneNumber)

        if (!isDelete) {
            if (phoneNumber.length >= 3) {
                stringBuilder.insert(3, " ")
            }

            if (phoneNumber.length >= 6) {
                stringBuilder.insert(7, " ")
            }
        }

        if (isDelete) {
            if (phoneNumber.length >= 3 && operateIndex != 3) {
                stringBuilder.insert(3, " ")
            }

            if (phoneNumber.length >= 6 && operateIndex != 7 && operateIndex != 3) {
                stringBuilder.insert(7, " ")
            }
        }

        return stringBuilder.toString()
    }
}