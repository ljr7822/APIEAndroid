package com.xiaoxun.apie.apie_data_loader.request.account.sms

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

/**
 * 发送验证码请求
 */
class SendSmsCodeParams(
    private val phoneNum: String,
    private val userId: String
) : APieRequestParams<SmsCodeModel>() {
    override fun apiService(version: String): Observable<BaseResponse<SmsCodeModel>>? {
        return APieApiManager.getAccountAPIService().sendSmsCode(
            phoneNum = phoneNum,
            userId = userId
        )
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_SEND_SMS_CODE.name}_${phoneNum}_${userId}"
    }

    override fun getVersion(data: SmsCodeModel): String {
        return "${APieUrl.ACCOUNT_SEND_SMS_CODE.name}_${phoneNum}_${userId}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_SEND_SMS_CODE.url
    }

    override fun needCache(data: SmsCodeModel): Boolean {
        return true
    }
}