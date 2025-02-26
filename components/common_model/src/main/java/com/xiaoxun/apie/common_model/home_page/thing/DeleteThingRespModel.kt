package com.xiaoxun.apie.common_model.home_page.thing

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class DeleteThingRespModel(
    @SerializedName("thingId")
    val thingId: String = ""
): Parcelable