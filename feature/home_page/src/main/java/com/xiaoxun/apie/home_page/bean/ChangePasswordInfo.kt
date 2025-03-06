package com.xiaoxun.apie.home_page.bean

import androidx.annotation.Keep

@Keep
data class ChangePasswordInfo(
    val oldPassword: String,
    val newPassword: String,
)
