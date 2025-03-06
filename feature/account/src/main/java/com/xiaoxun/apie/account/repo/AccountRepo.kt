package com.xiaoxun.apie.account.repo

import androidx.appcompat.app.AppCompatActivity
import com.xiaoxun.apie.account.viewmodel.AccountViewModel
import com.xiaoxun.apie.account_manager.repo.AccountDBRepository
import com.xiaoxun.apie.account_manager.repo.AccountDataDescriptor
import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.key.PublicKeyParams
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.password.LoginByPasswordRequestBody
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequest
import com.xiaoxun.apie.apie_data_loader.request.account.login.smscode.LoginBySmsCodeRequestBody
import com.xiaoxun.apie.apie_data_loader.request.account.sms.STSTokenParams
import com.xiaoxun.apie.apie_data_loader.request.account.sms.SendSmsCodeParams
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.repo.AliyunMMKVRepository
import com.xiaoxun.apie.common.repo.DesireMMKVRepository
import com.xiaoxun.apie.common.repo.ExecuteResultDelegate
import com.xiaoxun.apie.common.repo.PlanMMKVRepository
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.rsa.PasswordEncryptorUtils
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.account.PublicKeyModel
import com.xiaoxun.apie.common_model.sms.STSTokenModel
import com.xiaoxun.apie.common_model.sms.SmsCodeModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import io.reactivex.disposables.CompositeDisposable

class AccountRepo(
    private val activity: AppCompatActivity,
    private val viewModel: AccountViewModel
) : IAccountRepo, ExecuteResultDelegate {

    companion object {
        private const val TAG = "AccountRepo"
    }

    override val disposables = CompositeDisposable()

    private val accountDb: AccountDBRepository by lazy { AccountDBRepository(activity) }

    /**
     * 在生命周期结束时清理订阅
     */
    override fun onCleared() {
        disposables.clear()
    }

    override suspend fun startLoginByPassword(phoneNum: String, password: String) {
        viewModel.markLoginLoading()
        val encodePassword = AccountMMKVRepository.publicKey?.let {
            PasswordEncryptorUtils.encrypt(password, it)
        }?: let {
            APieLog.e(TAG, "startLoginByPassword failed: publicKey is null")
            viewModel.onLoginFailed("登录异常，请稍后再试")
            return
        }
        val result = loginByPassword(phoneNum, encodePassword)
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

    override suspend fun getSmsCode(phoneNum: String) {
        viewModel.getSmsCodeStart()
        val result = innerSmsCode(phoneNum)
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

    override suspend fun getSTSToken() {
        innerGetSTSToken().fold(
            onSuccess = {
                it.data?.let { resp ->
                    APieLog.d(TAG, "getSTSToken: $resp")
                    saveAliyunData2MMKV(resp)
                } ?: let {
                    APieLog.e(TAG, "getSTSToken failed: data is null")
                }
            },
            onFailure = {
                APieLog.e(TAG, "getSTSToken failed: $it")
            }
        )
    }

    override suspend fun getPublicKey() {
        innerGetPublicKey().fold(
            onSuccess = {
                it.data?.let { resp ->
                    APieLog.d(TAG, "getPublicKey: $resp")
                    AccountMMKVRepository.publicKey = resp.publicKey
                } ?: let {
                    APieLog.e(TAG, "getPublicKey failed: data is null")
                }
            },
            onFailure = {
                APieLog.e(TAG, "getPublicKey failed: $it")
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
        AccountMMKVRepository.userAvatar = accountModel.userPortrait ?: ""
        AccountMMKVRepository.userDesc = accountModel.desc ?: ""
        AccountMMKVRepository.token = accountModel.token
        AccountMMKVRepository.goldCount = accountModel.goldCount
        AccountMMKVRepository.sex = accountModel.sex
        AccountMMKVRepository.address = accountModel.address
        AccountMMKVRepository.school = accountModel.school ?: ""
        AccountMMKVRepository.totalThingPrice = accountModel.thingCard?.totalThingPrice ?: 0.0
        AccountMMKVRepository.thingCount = accountModel.thingCard?.totalThingCount ?: 0
        PlanMMKVRepository.planCount = accountModel.totalPlan
        DesireMMKVRepository.desireCount = accountModel.totalDesire
    }
    private fun saveAliyunData2MMKV(stsTokenModel: STSTokenModel) {
        AliyunMMKVRepository.securityToken = stsTokenModel.securityToken
        AliyunMMKVRepository.accessKeyId = stsTokenModel.accessKeyId
        AliyunMMKVRepository.accessKeySecret = stsTokenModel.accessKeySecret
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
        phoneNum: String
    ): Result<BaseResponse<SmsCodeModel>> {
        return executeResult {
            DataLoaderManager.instance.sendSmsCode(
                SendSmsCodeParams(phoneNum),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerGetSTSToken(): Result<BaseResponse<STSTokenModel>> {
        return executeResult {
            DataLoaderManager.instance.getSTSToken(
                STSTokenParams(AccountMMKVRepository.userId ?: ""),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerGetPublicKey(): Result<BaseResponse<PublicKeyModel>> {
        return executeResult {
            DataLoaderManager.instance.getPublicKey(
                PublicKeyParams(),
                CacheStrategy.FORCE_NET
            )
        }
    }
}