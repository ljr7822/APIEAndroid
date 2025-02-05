package com.xiaoxun.apie.common_model.home_page.group

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.xiaoxun.apie.common_model.home_page.base.IBaseGroupModel
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class PlanGroupModel(
    @SerializedName("groupId")
    override val groupId: String = "",
    @SerializedName("createUserId")
    override val createUserId: String = "",
    @SerializedName("groupName")
    override val groupName: String = "",
    @SerializedName("groupIcon")
    override val groupIcon: String = "",
    @SerializedName("groupWeight")
    override val groupWeight: String = "",
    @SerializedName("visibleStatus")
    override val visibleStatus: String = "",
    @SerializedName("createat")
    val createat: String = "",
    @SerializedName("updateat")
    val updateat: String = "",
) : Parcelable, IBaseGroupModel