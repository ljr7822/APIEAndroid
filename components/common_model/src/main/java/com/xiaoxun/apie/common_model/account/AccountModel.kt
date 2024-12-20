package com.xiaoxun.apie.common_model.account

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 用户的接口回调数据模型
 */
@Parcelize
@Keep
data class AccountModel(
    @SerializedName("userId")
    var userId: String = "",
    @SerializedName("userName")
    val userName: String? = "",
    @SerializedName("phoneNum")
    var phoneNum: String = "",
    @SerializedName("token")
    var token: String? = "",
    @SerializedName("desc")
    val desc: String? = "",
    @SerializedName("sex")
    val sex: Int? = null,
    @SerializedName("address")
    var address: String = "",
    @SerializedName("school")
    val school: String? = ""
) : Parcelable