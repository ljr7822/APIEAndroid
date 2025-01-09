package com.xiaoxun.apie.common_model.account

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * 用户的接口回调数据模型
 * {
 *     "code": 0,
 *     "message": "ok",
 *     "data": {
 *         "userId": "99678322425856",
 *         "userName": "小寻测试1",
 *         "userPortrait": "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
 *         "phoneNum": "18289816889",
 *         "token": "ZGZhYTVhMTYtYTE1Ni00YTI4LThlM2ItZWEyMzM4MTVlNDI1",
 *         "desc": "小寻的测试账号",
 *         "sex": 0,
 *         "address": "中国-北京-朝阳区",
 *         "school": "清华大学",
 *         "birthday": "2025-01-01T02:45:36.000+00:00",
 *         "grade": 1000,
 *         "goldCount": 9999,
 *         "userType": 10
 *     }
 * }
 */
@Parcelize
@Keep
data class AccountModel(
    @SerializedName("userId")
    var userId: String = "",
    @SerializedName("userName")
    val userName: String? = "",
    @SerializedName("userPortrait")
    val userPortrait: String? = "",
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
    val school: String? = "",
    @SerializedName("grade")
    val grade: Int? = null,
    @SerializedName("goldCount")
    val goldCount: Int? = null,
    @SerializedName("userType")
    val userType: Int? = null,
) : Parcelable