package com.xiaoxun.apie.common_model.account

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class PublicKeyModel(
    @SerializedName("publicKey")
    val publicKey: String? = ""
): Parcelable
