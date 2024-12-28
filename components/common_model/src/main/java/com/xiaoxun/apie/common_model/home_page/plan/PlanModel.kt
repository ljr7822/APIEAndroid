package com.xiaoxun.apie.common_model.home_page.plan

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Keep
data class PlanModel(
    @SerializedName("planId")
    val planId: String = "",
    @SerializedName("planName")
    val planName: String = "",
    @SerializedName("planIcon")
    val planIcon: String = "",
    @SerializedName("belongGroupId")
    val belongGroupId: String = "",
    @SerializedName("planFrequency")
    val planFrequency: String = "",
    @SerializedName("planAward")
    val planAward: Int = 1,
    @SerializedName("planPunish")
    val planPunish: Int = 1,
    @SerializedName("planWeight")
    val planWeight: Int = 10,
    @SerializedName("visibleStatus")
    val visibleStatus: Int = 1,
    @SerializedName("createUserId")
    val createUserId: String = "",
    @SerializedName("createat")
    val createat: String = "",
    @SerializedName("updateat")
    val updateat: String = "",
): Parcelable