package com.xiaoxun.apie.common_model.sms

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class SmsCodeModel (
    @SerializedName("smsCode")
    val smsCode: String? = null,
    @SerializedName("phoneNum")
    val phoneNum: String? = null,
    // 来源1-请求阿里云，2-复用redis
    @SerializedName("source")
    val source: String? = null
) : Parcelable
