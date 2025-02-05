package com.xiaoxun.apie.apie_data_loader.request.group

import com.xiaoxun.apie.apie_data_loader.APieUrl
import com.xiaoxun.apie.apie_data_loader.api.APieApiManager
import com.xiaoxun.apie.apie_data_loader.request.APieRequestParams
import com.xiaoxun.apie.common_model.home_page.group.DeleteGroupRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import io.reactivex.Observable

class DeleteGroup (
    private val groupId: String
) : APieRequestParams<DeleteGroupRespModel>() {
    override fun apiService(version: String): Observable<BaseResponse<DeleteGroupRespModel>>? {
        return APieApiManager.getPlanGroupAPIService().deleteGroup(groupId)
    }

    override fun dataType(): String {
        return "${APieUrl.DELETE_GROUP.name}_${groupId}"
    }

    override fun getVersion(data: DeleteGroupRespModel): String {
        return "${APieUrl.DELETE_GROUP.name}_${groupId}"
    }

    override fun url(): String {
        return APieUrl.DELETE_GROUP.url
    }

    override fun needCache(data: DeleteGroupRespModel): Boolean {
        return false
    }
}