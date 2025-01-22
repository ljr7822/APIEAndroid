package com.xiaoxun.apie.common_model.sms

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class STSTokenModel(
    @SerializedName("securityToken")
    val securityToken: String? = null,
    @SerializedName("accessKeyId")
    val accessKeyId: String? = null,
    @SerializedName("accessKeySecret")
    val accessKeySecret: String? = null
): Parcelable