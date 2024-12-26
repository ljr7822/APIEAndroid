package com.xiaoxun.apie.common.utils

class PhoneNumberValidator {
    companion object {
        private const val PHONE_REGEX = "^(1[3-9]\\d{9})$"

        @JvmStatic
        fun isValidPhoneNumber(phoneNumber: String): Boolean {
            return phoneNumber.matches(PHONE_REGEX.toRegex())
        }
    }
}