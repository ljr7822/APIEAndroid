package com.xiaoxun.apie.apie_data_loader.request.account.login.password

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * 登录需要的参数
 */
@Parcelize
@Keep
data class LoginByPasswordRequestBody(
    var phoneNum: String? = null,
    var password: String? = null,
) : Parcelable
