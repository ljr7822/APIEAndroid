package com.xiaoxun.apie.common_model.home_page.desire

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class CommonDesireRespModel(
    @SerializedName("desireId")
    val desireId: String = ""
): Parcelable