package com.xiaoxun.apie.apie_data_loader

enum class APieUrl(val url: String) {
    ACCOUNT_LOGIN_PASSWORD(ACCOUNT_LOGIN_PASSWORD_URL),
    ACCOUNT_LOGIN_SMS_CODE(ACCOUNT_LOGIN_SMS_CODE_URL),
    ACCOUNT_SEND_SMS_CODE(ACCOUNT_SEND_SMS_CODE_URL),
    ACCOUNT_REGISTER(ACCOUNT_REGISTER_URL),
}

internal const val ACCOUNT_LOGIN_PASSWORD_URL = "/user/loginByPassword"
internal const val ACCOUNT_LOGIN_SMS_CODE_URL = "/user/loginBySmsCode"
internal const val ACCOUNT_SEND_SMS_CODE_URL = "/sms/sendSMSCode/{phoneNum}/{userId}"
internal const val ACCOUNT_REGISTER_URL = "/user/loginByPassword"
