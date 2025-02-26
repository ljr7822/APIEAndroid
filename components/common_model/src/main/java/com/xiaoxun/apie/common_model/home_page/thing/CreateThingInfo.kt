package com.xiaoxun.apie.common_model.home_page.thing

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Keep
data class CreateThingInfo(
    val thingName: String,
    val belongGroupId: String,
    val thingPrice: Double,
    val thingTags: MutableList<String>,
    val thingIcon: String,
    val thingStatus: Int,
    val thingDesc: String,
    val buyAt: Date,
    val warrantyPeriod: Date,
    val thingAppendixList: MutableList<String>
): Parcelable