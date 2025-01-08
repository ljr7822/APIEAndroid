package com.xiaoxun.apie.common_model.home_page.plan

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import kotlinx.parcelize.Parcelize

/**
 * 计划类型(planType)，计划有以下几种：
 * 1、单次计划，即只有一次的计划(次数(planFrequency)=1)
 * 2、每日计划，即每天都要完成的计划(次数(planFrequency)可自定义)
 * 3、每周计划，即每周都要完成的计划(次数(planFrequency)可自定义)
 * 3、每月计划，即每月都要完成的计划(次数(planFrequency)可自定义)
 * 4、每年计划，即每年都要完成的计划(次数(planFrequency)可自定义)
 * 5、自定义计划(定目标)，即自定义时间段内都要完成的计划(次数(planFrequency)可自定义)
 *
 * 计划开始时间(planStartTime)
 * 计划截止时间(planStopTime)
 *
 * 计划进行状态(planStatus)：0: 未开始，1: 进行中，2: 已完成，3: 已放弃
 */
@Parcelize
@Keep
data class PlanModel(
    // 计划id
    @SerializedName("planId")
    val planId: String = "",
    // 计划名称
    @SerializedName("planName")
    val planName: String = "",
    // 计划图标
    @SerializedName("planIcon")
    val planIcon: String = "",
    // 计划所在分组id
    @SerializedName("belongGroupId")
    val belongGroupId: String = "",
    // 计划所在分组信息
    @SerializedName("planGroupBean")
    val planGroupModel: PlanGroupModel,
    // 计划类型
    @SerializedName("planType")
    val planType: Int = 0,
    // 计划频率
    @SerializedName("planFrequency")
    val planFrequency: Int = 0,
    // 计划完成奖励
    @SerializedName("planAward")
    val planAward: Int = 1,
    // 计划未完成惩罚
    @SerializedName("planPunish")
    val planPunish: Int = 1,
    // 计划权重
    @SerializedName("planWeight")
    val planWeight: Int = 10,
    // 计划状态：1可见，0不可见
    @SerializedName("visibleStatus")
    val visibleStatus: Int = 1,
    // 计划已完成次数
    @SerializedName("planCompletedCount")
    val planCompletedCount: Int = 0,
    // 计划创建者id
    @SerializedName("createUserId")
    val createUserId: String = "",
    // 计划创建时间
    @SerializedName("createat")
    val createat: String = "",
    // 计划更新时间
    @SerializedName("updateat")
    val updateat: String = "",
    // 计划开始时间
    @SerializedName("planStartTime")
    val planStartTime: Long = 0,
    // 计划截止时间
    @SerializedName("planStopTime")
    val planStopTime: Long = 0,
    // 计划进行状态
    @SerializedName("planStatus")
    val planStatus: Int = 0,
): Parcelable