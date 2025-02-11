package com.xiaoxun.apie.common_model.home_page.storage.group

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.xiaoxun.apie.common_model.home_page.base.IBaseGroupModel
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Keep
data class ThingGroupModel(
    @SerializedName("thingGroupId")
    override val groupId: String = "",
    @SerializedName("createUserId")
    override val createUserId: String = "",
    @SerializedName("thingGroupName")
    override val groupName: String = "",
    @SerializedName("thingGroupIcon")
    override val groupIcon: String = "",
    @SerializedName("thingGroupWeight")
    override val groupWeight: String = "",
    @SerializedName("visibleStatus")
    override val visibleStatus: String = "",
    // 分组类型：0-系统默认分组，1-用户自定义分组
    @SerializedName("thingGroupType")
    val thingGroupType: Int = 0,
    @SerializedName("createat")
    val createat: Date? = null,
    @SerializedName("updateat")
    val updateat: Date? = null,
) : Parcelable, IBaseGroupModel