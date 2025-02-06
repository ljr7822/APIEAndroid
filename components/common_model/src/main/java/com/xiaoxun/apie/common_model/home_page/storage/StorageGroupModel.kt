package com.xiaoxun.apie.common_model.home_page.storage

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.xiaoxun.apie.common_model.home_page.base.IBaseGroupModel
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Keep
data class StorageGroupModel(
    @SerializedName("desireGroupId")
    override val groupId: String = "",
    @SerializedName("createUserId")
    override val createUserId: String = "",
    @SerializedName("desireGroupName")
    override val groupName: String = "",
    @SerializedName("desireGroupIcon")
    override val groupIcon: String = "",
    @SerializedName("desireGroupWeight")
    override val groupWeight: String = "",
    @SerializedName("visibleStatus")
    override val visibleStatus: String = "",
    @SerializedName("createat")
    val createat: Date? = null,
    @SerializedName("updateat")
    val updateat: Date? = null,
) : Parcelable, IBaseGroupModel