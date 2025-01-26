package com.xiaoxun.apie.common_model.home_page.desire.group

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class DesireGroupRespModel(
    @SerializedName("desireGroupList")
    val desireGroupList: List<DesireGroupModel> = emptyList()
): Parcelable