package com.xiaoxun.apie.common_model.home_page.desire

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * {
 *         "desireId": "11064473972375552",
 *         "desireName": "买十台特斯拉送粉丝",
 *         "desireIcon": "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
 *         "desireBelongGroupId": "103415245963264",
 *         "visibleStatus": 1,
 *         "desirePrice": 899,
 *         "desireType": 1,
 *         "desireWeight": 10,
 *         "createUserId": "99678322425856",
 *         "desireTotalCount": 1,
 *         "desireExchangeCount": 1,
 *         "createat": "2025-01-15T12:46:16.163+00:00",
 *         "updateat": "2025-01-15T12:46:16.163+00:00"
 *     }
 */
@Parcelize
@Keep
data class DesireModel(
    // 心愿id
    @SerializedName("desireId")
    val desireId: String = "",
    // 心愿名称
    @SerializedName("desireName")
    val desireName: String = "",
    // 心愿图标
    @SerializedName("desireIcon")
    val desireIcon: String = "",
    // 心愿所在分组id
    @SerializedName("desireBelongGroupId")
    val desireBelongGroupId: String = "",
    // 心愿状态：1可见，0不可见
    @SerializedName("visibleStatus")
    val visibleStatus: Int = 1,
    // 心愿价格
    @SerializedName("desirePrice")
    val desirePrice: Int,
    // 心愿类型
    @SerializedName("desireType")
    val desireType: Int = 1,
    // 心愿权重
    @SerializedName("desireWeight")
    val desireWeight: Int = 0,
    // 创建者id
    @SerializedName("createUserId")
    val createUserId: String = "",
    // 心愿总数
    @SerializedName("desireTotalCount")
    val desireTotalCount: Int,
    // 心愿兑换数
    @SerializedName("desireExchangeCount")
    val desireExchangeCount: Int = 0,
    // 创建时间
    @SerializedName("createat")
    val createat: Date,
    // 更新时间
    @SerializedName("updateat")
    val updateat: Date

) : Parcelable
