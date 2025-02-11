package com.xiaoxun.apie.common_model.home_page.storage.group

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ThingGroupRespModel(
    @SerializedName("thingGroupList")
    val thingGroupList: List<ThingGroupModel> = emptyList()
): Parcelable