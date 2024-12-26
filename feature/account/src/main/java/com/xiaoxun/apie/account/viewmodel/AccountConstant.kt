package com.xiaoxun.apie.account.viewmodel

enum class LoginWayType(val type: Int) {
    PASSWORD(0),
    SMS_CODE(1)
}

enum class LoadingState {
    Initialization, Loading, Success, Failed
}

enum class SendSmsCodeStatus {
    SendSuccess, SendFailed
}