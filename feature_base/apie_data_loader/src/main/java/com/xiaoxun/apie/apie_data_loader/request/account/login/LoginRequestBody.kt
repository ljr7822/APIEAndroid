package com.xiaoxun.apie.apie_data_loader.request.account.login

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * 登录需要的参数
 */
@Parcelize
@Keep
data class LoginRequestBody(
    var account: String? = null,
    var password: String? = null,
    var pushId: String? = null
) : Parcelable
