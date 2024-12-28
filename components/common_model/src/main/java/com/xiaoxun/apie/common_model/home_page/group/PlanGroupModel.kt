package com.xiaoxun.apie.common_model.home_page.group

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class PlanGroupModel(
    @SerializedName("groupId")
    val groupId: String = "",
    @SerializedName("createUserId")
    val createUserId: String = "",
    @SerializedName("groupName")
    val groupName: String = "",
    @SerializedName("groupIcon")
    val groupIcon: String = "",
    @SerializedName("groupWeight")
    val groupWeight: String = "",
    @SerializedName("visibleStatus")
    val visibleStatus: String = "",
    @SerializedName("createat")
    val createat: String = "",
    @SerializedName("updateat")
    val updateat: String = "",
): Parcelable