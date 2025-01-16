package com.xiaoxun.apie.apie_data_loader.request.desire

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * {
 *   "desireName": "买一台特斯拉送粉丝",
 *   "desireIcon": "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
 *   "desireBelongGroupId": "103415245963264",
 *   "desirePrice":899,
 *   "desireType": 1,
 *   "desireWeight": 10,
 *   "createUserId":"99678322425856",
 *   "desireTotalCount": 1,
 *   "desireExchangeCount": 1
 * }
 */
@Parcelize
@Keep
data class CreateDesireRequestBody(
    val desireName: String = "",
    val desireIcon: String = "",
    val desireBelongGroupId: String = "",
    val desirePrice: Int,
    val desireType: Int = 1,
    val desireWeight: Int = 0,
    val createUserId: String = "",
    val desireTotalCount: Int,
    val desireExchangeCount: Int = 0
): Parcelable