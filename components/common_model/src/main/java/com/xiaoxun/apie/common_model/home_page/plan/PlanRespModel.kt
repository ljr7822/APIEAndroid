package com.xiaoxun.apie.common_model.home_page.plan

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class PlanRespModel(
    @SerializedName("planList")
    val planList: List<PlanModel> = emptyList()
): Parcelable