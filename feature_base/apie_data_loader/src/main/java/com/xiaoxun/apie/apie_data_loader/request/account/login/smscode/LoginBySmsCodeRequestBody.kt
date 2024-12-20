package com.xiaoxun.apie.apie_data_loader.request.account.login.smscode

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * 登录需要的参数
 */
@Parcelize
@Keep
data class LoginBySmsCodeRequestBody(
    var phoneNum: String? = null,
    var smsCode: String? = null,
) : Parcelable
