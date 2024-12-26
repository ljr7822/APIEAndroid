package com.xiaoxun.apie.data_loader.data

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

    @SerializedName("success")
    var success: Boolean? = null

    @SerializedName("code")
    var code: Int? = null

    @SerializedName("data")
    var data: T? = null

    @SerializedName("message")
    var message: String? = null

    override fun toString(): String {
        return "BaseResponse: success=$success, code=$code, data=$data, message=$message"
    }

    fun isSuccess(): Boolean {
        return code == 0
    }
}