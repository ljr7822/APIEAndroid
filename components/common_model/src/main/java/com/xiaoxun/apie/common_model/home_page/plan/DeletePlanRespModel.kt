package com.xiaoxun.apie.common_model.home_page.plan

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class DeletePlanRespModel(
    @SerializedName("planId")
    val planId: String = ""
): Parcelable