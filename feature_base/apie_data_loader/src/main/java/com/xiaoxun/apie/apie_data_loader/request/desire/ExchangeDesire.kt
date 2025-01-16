package com.xiaoxun.apie.apie_data_loader.request.desire

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.desire.CommonDesireRespModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class ExchangeDesire(
    private val desireId: String
) : APieRequestParams<DesireModel>() {
    override fun apiService(version: String): Observable<BaseResponse<DesireModel>>? {
        return APieApiManager.getDesireAPIService().exchangeDesire(desireId)
    }

    override fun dataType(): String {
        return "${APieUrl.EXCHANGE_DESIRE.name}_${desireId}"
    }

    override fun getVersion(data: DesireModel): String {
        return "${APieUrl.EXCHANGE_DESIRE.name}_${desireId}"
    }

    override fun url(): String {
        return APieUrl.EXCHANGE_DESIRE.url
    }

    override fun needCache(data: DesireModel): Boolean {
        return false
    }
}