package com.xiaoxun.apie.apie_data_loader.request.plan

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * {
 *     "groupName":"早睡",
 *     "createUserId":"99678322425856",
 *     "groupIcon": "www.baidu.com",
 *     "groupWeight": 10
 * }
 */
@Parcelize
@Keep
data class CreatePlanGroupRequestBody(
    val groupName: String,
    val groupIcon: String = "",
    val groupWeight: Int = 10,
    val createUserId: String,
): Parcelable