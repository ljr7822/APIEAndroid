package com.xiaoxun.apie.data_loader.data

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("code")
    var code: Int? = null

    @SerializedName("result")
    var data: T? = null

    @SerializedName("message")
    var message: String? = null

}