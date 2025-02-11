package com.xiaoxun.apie.common_model.home_page.storage

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.xiaoxun.apie.common.utils.DateTimeUtils
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.storage.group.ThingGroupModel
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.concurrent.TimeUnit

@Parcelize
@Keep
data class ThingItemModel(
    // 物品id
    @SerializedName("thingId")
    val thingId: String = "",
    // 物品名称
    @SerializedName("thingName")
    val thingName: String = "",
    // 物品主人id
    @SerializedName("createUserId")
    val createUserId: String = "",
    // 物品分组id
    @SerializedName("belongGroupId")
    val belongGroupId: String = "",
    // 物品所在分组信息
    @SerializedName("thingGroupBean")
    val thingGroupBean: ThingGroupModel,
    // 物品价格
    @SerializedName("thingPrice")
    val thingPrice: Float = 0f,
    // 物品标签
    @SerializedName("thingTags")
    val thingTags: MutableList<ThingTagModel>? = null,
    // 物品图片
    @SerializedName("thingIcon")
    val thingIcon: String = "",
    // 物品状态：1-使用中，2-已退役，3-已损坏，4-已丢失，5-吃灰中，6-已售出
    @SerializedName("thingStatus")
    val thingStatus: Int = 1,
    // 物品使用天数
    @SerializedName("useDays")
    val useDays: Int = 0,
    // 物品描述
    @SerializedName("thingDesc")
    val thingDesc: String = "",
    // 物品购买时间
    @SerializedName("buyAt")
    val buyAt: Date? = null,
    // 物品创建时间
    @SerializedName("createAt")
    val createAt: Date? = null,
    // 物品更新时间
    @SerializedName("updateAt")
    val updateAt: Date? = null,
    // 物品保修期
    @SerializedName("warrantyPeriod")
    val warrantyPeriod: Date? = null,
) : Parcelable {

    fun getThingStatusDesc(): String {
        return when (thingStatus) {
            1 -> "使用中"
            2 -> "已退役"
            3 -> "已损坏"
            4 -> "已丢失"
            5 -> "吃灰中"
            6 -> "已售出"
            else -> "未知状态"
        }
    }

    fun getBuyAtDesc(): String {
        return buyAt?.let {
            DateTimeUtils.formatDate(it)
        } ?: ""
    }

    fun checkWarrantyStatus(): String {
        return warrantyPeriod?.let {
            if (Date().after(it)) "过保" else "在保"
        } ?: "未知"
    }

    fun daysSince(): Long {
        return buyAt?.let {
            val diffInMillis = Date().time - it.time // 当前时间 - 购买时间
            TimeUnit.MILLISECONDS.toDays(diffInMillis) // 转换为天数
        } ?: 0
    }

    fun getAveragePrice(): String {
        val averagePrice = thingPrice / daysSince()
        return averagePrice.toString()
    }
}

@Parcelize
@Keep
data class ThingTagModel(
    @SerializedName("tagId")
    val tagId: String = "",
    @SerializedName("tagName")
    val tagName: String = "",
    @SerializedName("tagIcon")
    val tagIcon: String = ""
) : Parcelable
