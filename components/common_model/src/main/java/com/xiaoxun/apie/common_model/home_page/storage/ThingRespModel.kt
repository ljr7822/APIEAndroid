package com.xiaoxun.apie.common_model.home_page.storage

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class ThingRespModel(
    @SerializedName("thingList")
    val thingList: List<ThingItemModel> = emptyList()
): Parcelable