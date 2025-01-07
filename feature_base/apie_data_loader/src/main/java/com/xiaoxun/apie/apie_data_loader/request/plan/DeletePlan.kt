package com.xiaoxun.apie.apie_data_loader.request.plan

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class DeletePlan (
    private val planId: String
) : APieRequestParams<Int>() {
    override fun apiService(version: String): Observable<BaseResponse<Int>>? {
        return APieApiManager.getPlanAPIService().deletePlan(planId)
    }

    override fun dataType(): String {
        return "${APieUrl.ACCOUNT_GET_ALL_PLAN_BY_USER_ID.name}_${planId}"
    }

    override fun getVersion(data: Int): String {
        return "${APieUrl.ACCOUNT_GET_ALL_PLAN_BY_USER_ID.name}_${planId}"
    }

    override fun url(): String {
        return APieUrl.ACCOUNT_GET_ALL_PLAN_BY_USER_ID.url
    }

    override fun needCache(data: Int): Boolean {
        return false
    }
}