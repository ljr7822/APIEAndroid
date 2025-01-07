package com.xiaoxun.apie.apie_data_loader.request.plan

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * {
 *   "planName": "这是一个新计划",
 *   "planIcon": "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
 *   "belongGroupId": "103415245963264",
 *   "planFrequency": 1,
 *   "planAward": 1,
 *   "planPunish": 1,
 *   "planWeight": 10,
 *   "createUserId":"99678322425856"
 * }
 */
@Parcelize
@Keep
data class CreatePlanRequestBody(
    val planName: String = "",
    val planIcon: String = "",
    val planType: Int = 1,
    val belongGroupId: String = "",
    val planFrequency: Int = 1,
    val planAward: Int = 0,
    val planPunish: Int = 0,
    val planWeight: Int = 0,
    val createUserId: String = ""
): Parcelable