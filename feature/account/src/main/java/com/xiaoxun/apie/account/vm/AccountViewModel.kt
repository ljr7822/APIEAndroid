package com.xiaoxun.apie.account.vm

import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.login.LoginRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.LoginRequestBody
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy

class AccountViewModel: APieBaseViewModel() {
    suspend fun login(
        account: String,
        password: String,
        pushId: String
    ): Result<BaseResponse<AccountModel>> {
        val loginRequestBody = LoginRequestBody(account, password, pushId)
        return executeResult {
            DataLoaderManager.instance.login(
                LoginRequest(loginRequestBody),
                CacheStrategy.FORCE_NET
            )
        }
    }

}