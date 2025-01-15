package com.xiaoxun.apie.account.repo

import androidx.appcompat.app.AppCompatActivity
import com.xiaoxun.apie.account.viewmodel.AccountViewModel
import com.xiaoxun.apie.account_manager.repo.AccountDBRepository
import com.xiaoxun.apie.account_manager.repo.AccountDataDescriptor
import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequestBody
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequestBody
import com.xiaoxun.apie.apie_data_loader.request.account.sms.SendSmsCode
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.repo.DesireMMKVRepository
import com.xiaoxun.apie.common.repo.PlanMMKVRepository
import com.xiaoxun.apie.common.utils.coroutine.singleSuspendCoroutine
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.resume

class AccountRepo(
    private val activity: AppCompatActivity,
    private val viewModel: AccountViewModel
) : IAccountRepo {

    private val disposables = CompositeDisposable()

    private val accountDb: AccountDBRepository by lazy { AccountDBRepository(activity) }

    /**
     * 在生命周期结束时清理订阅
     */
    override fun onCleared() {
        disposables.clear()
    }

    override suspend fun startLoginByPassword(phoneNum: String, password: String) {
        viewModel.markLoginLoading()
        val result = loginByPassword(phoneNum, password)
        result.fold(
            onSuccess = {
                val response = it
                if (response.isSuccess()) {
                    response.data?.let { accountModel ->
                        viewModel.onLoginSuccess()
                        saveAccountData2DB(accountModel)
                    } ?: let {
                        viewModel.onLoginFailed("登录异常，用户数据为空")
                    }
                } else {
                    viewModel.onLoginFailed(response.message ?: "登录异常，请稍后再试")
                }
            },
            onFailure = {
                viewModel.onLoginFailed(it.message ?: "登录异常，请稍后再试")
            }
        )
    }

    override suspend fun startLoginBySmsCode(phoneNum: String, smsCode: String) {
        viewModel.markLoginLoading()
        val result = loginBySmsCode(phoneNum, smsCode)
        result.fold(
            onSuccess = {
                val response = it
                if (response.isSuccess()) {
                    response.data?.let { accountModel ->
                        viewModel.onLoginSuccess()
                        saveAccountData2DB(accountModel)
                    } ?: let {
                        viewModel.onLoginFailed("登录异常，用户数据为空")
                    }
                } else {
                    viewModel.onLoginFailed(response.message ?: "登录异常，请稍后再试")
                }
            },
            onFailure = {
                viewModel.onLoginFailed(it.message ?: "登录异常，请稍后再试")
            }
        )
    }

    override suspend fun getSmsCode(phoneNum: String, userId: String) {
        val result = innerSmsCode(phoneNum, userId)
        result.fold(
            onSuccess = {
                val response = it
                if (response.isSuccess()) {
                    response.data?.let {
                        viewModel.sendSmsCodeSuccess()
                    } ?: let {
                        viewModel.sendSmsCodeFailed()
                    }
                } else {
                    viewModel.sendSmsCodeFailed()
                }
            },
            onFailure = {
                viewModel.sendSmsCodeFailed()
            }
        )
    }

    private fun saveAccountData2DB(accountModel: AccountModel) {
        // 保存用户数据到数据库
        accountDb.insertAccountData(
            AccountDataDescriptor(
                userId = accountModel.userId,
                userName = accountModel.userName,
                userPortrait = accountModel.userPortrait,
                phoneNumber = accountModel.phoneNum,
                token = accountModel.token,
                desc = accountModel.desc,
                sex = accountModel.sex,
                address = accountModel.address,
                grade = accountModel.grade,
                userType = accountModel.userType
            )
        )
        saveAccountData2MMKV(accountModel)
    }

    /**
     * 更新userId\token到MMKV
     */
    private fun saveAccountData2MMKV(accountModel: AccountModel) {
        AccountMMKVRepository.userId = accountModel.userId
        AccountMMKVRepository.userName = accountModel.userName
        AccountMMKVRepository.token = accountModel.token
        AccountMMKVRepository.goldCount = accountModel.goldCount

        PlanMMKVRepository.planCount = accountModel.totalPlan

        DesireMMKVRepository.desireCount = accountModel.totalDesire
    }

    private suspend fun loginByPassword(
        phoneNum: String,
        password: String
    ): Result<BaseResponse<AccountModel>> {
        return executeResult {
            DataLoaderManager.instance.loginByPassword(
                LoginByPasswordRequest(LoginByPasswordRequestBody(phoneNum, password)),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun loginBySmsCode(
        phoneNum: String,
        smsCode: String
    ): Result<BaseResponse<AccountModel>> {
        return executeResult {
            DataLoaderManager.instance.loginBySmsCode(
                LoginBySmsCodeRequest(LoginBySmsCodeRequestBody(phoneNum, smsCode)),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerSmsCode(
        phoneNum: String,
        userId: String
    ): Result<BaseResponse<SmsCodeModel>> {
        return executeResult {
            DataLoaderManager.instance.sendSmsCode(
                SendSmsCode(phoneNum, userId),
                CacheStrategy.FORCE_NET
            )
        }
    }

    /**
     * 用于执行 RxJava 的网络请求并返回结果
     */
    protected suspend fun <T> executeResult(
        block: () -> Observable<T>
    ): Result<T> = singleSuspendCoroutine { continuation ->
        val disposable = block.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    continuation.resume(Result.success(result))
                },
                { error ->
                    continuation.resume(Result.failure(error))
                }
            )
        disposables.add(disposable)
    }

    /**
     * 用于执行 RxJava 的网络请求，没有返回结果
     */
    protected suspend fun executeNoResult(
        block: () -> Observable<Any>
    ): Result<Unit> = singleSuspendCoroutine { continuation ->
        val disposable = block.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    continuation.resume(Result.success(Unit))
                },
                { error ->
                    continuation.resume(Result.failure(error))
                }
            )
        disposables.add(disposable)
    }
}