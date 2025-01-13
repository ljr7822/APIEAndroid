package com.xiaoxun.apie.apie_data_loader.request.group

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * {
 *     "groupId":"8501225687351296",
 *     "groupName":"修改名称了",
 *     "createUserId":"99678322425856",
 *     "groupIcon": "",
 *     "groupWeight": "10"
 * }
 */
@Parcelize
@Keep
data class UpdateGroupRequestBody(
    val groupId: String = "",
    val groupName: String = "",
    val groupIcon: String = "",
    val groupWeight: Int = 0,
    val createUserId: String = "",
): Parcelable