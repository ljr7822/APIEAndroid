package com.xiaoxun.apie.apie_data_loader.request.thing

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * {
 *     "thingGroupName":"早睡",
 *     "createUserId":"99678322425856",
 *     "thingGroupIcon": "www.baidu.com",
 *     "thingGroupWeight": 10
 * }
 */
@Parcelize
@Keep
data class CreateThingGroupRequestBody(
    val thingGroupName: String,
    val thingGroupIcon: String = "",
    val thingGroupWeight: Int = 10,
    val createUserId: String,
): Parcelable