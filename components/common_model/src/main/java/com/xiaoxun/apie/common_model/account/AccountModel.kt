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
    //@SerializedName("user")
    //val userBean: UserBean,
    // 当前登录的账号
    @SerializedName("account")
    var account: String? = null,
    // 当前登录成功后获取的Token, 可以通过Token获取用户的所有信息
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("isBind")
    // 标示是否已经绑定到了设备PushId
    val isBind: Boolean? = false
) : Parcelable