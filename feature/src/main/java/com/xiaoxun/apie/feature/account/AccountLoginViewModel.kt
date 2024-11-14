package com.xiaoxun.apie.feature.account

import androidx.lifecycle.ViewModel
import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.login.LoginRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.LoginRequestBody
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AccountLoginViewModel: ViewModel() {

    private var disposables = mutableListOf<Disposable?>()

    fun login(account: String, password: String,  pushId: String) {
        val loginRequestBody = LoginRequestBody(account, password, pushId)
        DataLoaderManager.instance
            .login(LoginRequest(loginRequestBody), CacheStrategy.FORCE_NET)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                APieLog.d("AccountLoginViewModel", "login success: $it")
            },{
                APieLog.d("AccountLoginViewModel", "login error: $it")
            }).let {
                disposables.add(it)
            }
    }
}