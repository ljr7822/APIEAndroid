package com.xiaoxun.apie.common_model.home_page.group

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class DeleteGroupRespModel(
    @SerializedName("groupId")
    val groupId: String = ""
): Parcelable