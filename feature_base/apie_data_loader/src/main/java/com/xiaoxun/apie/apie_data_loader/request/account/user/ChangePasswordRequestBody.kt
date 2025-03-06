package com.xiaoxun.apie.apie_data_loader.request.account.user

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * 修改密码需要的参数
 */
@Parcelize
@Keep
data class ChangePasswordRequestBody(
    var oldPassword: String? = null,
    var newPassword: String? = null,
) : Parcelable
