package com.xiaoxun.apie.apie_data_loader

enum class APieUrl(val url: String) {
    ACCOUNT_LOGIN(ACCOUNT_LOGIN_URL),
    ACCOUNT_REGISTER(ACCOUNT_REGISTER_URL),
}

internal const val ACCOUNT_LOGIN_URL = "/api/account/login"
internal const val ACCOUNT_REGISTER_URL = "/api/account/register"
