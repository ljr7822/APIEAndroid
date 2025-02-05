package com.xiaoxun.apie.apie_data_loader.request.desire

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * {
 *     "desireGroupName":"早睡",
 *     "createUserId":"99678322425856",
 *     "desireGroupIcon": "www.baidu.com",
 *     "desireGroupWeight": 10
 * }
 */
@Parcelize
@Keep
data class CreateDesireGroupRequestBody(
    val desireGroupName: String,
    val desireGroupIcon: String = "",
    val desireGroupWeight: Int = 10,
    val createUserId: String,
): Parcelable