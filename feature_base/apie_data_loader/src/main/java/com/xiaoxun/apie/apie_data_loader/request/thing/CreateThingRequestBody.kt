package com.xiaoxun.apie.apie_data_loader.request.thing

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 *{
 *     "thingName": "Apple watch S10 黑",
 *     "createUserId": "99678322425856",
 *     "belongGroupId": "20468758678077440",
 *     "thingPrice": 2299,
 *     "thingTags": [
 *         "手表",
 *         "数码"
 *     ],
 *     "thingIcon": "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
 *     "thingStatus": 1,
 *     "thingDesc": "大力送给自己的S10",
 *     "buyAt": "2025-02-10T01:53:35.221Z",
 *     "warrantyPeriod": "2025-12-10T13:53:00.852Z"
 * }
 */
@Parcelize
@Keep
data class CreateThingRequestBody(
    val thingName: String,
    val createUserId: String,
    val belongGroupId: String,
    val thingPrice: Double,
    val thingTags: MutableList<String>,
    val thingIcon: String,
    val thingStatus: Int,
    val thingDesc: String,
    val buyAt: Date,
    val warrantyPeriod: Date
): Parcelable