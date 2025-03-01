package com.example.xiaoxun.repo

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.xiaoxun.viewmodel.CheckLoginStatus
import com.example.xiaoxun.viewmodel.WelcomeViewModel
import com.xiaoxun.apie.account_manager.repo.AccountDBRepository
import com.xiaoxun.apie.account_manager.repo.AccountDataDescriptor
import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.sms.STSTokenParams
import com.xiaoxun.apie.apie_data_loader.request.account.user.QueryUser
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.repo.AliyunMMKVRepository
import com.xiaoxun.apie.common.repo.DesireMMKVRepository
import com.xiaoxun.apie.common.repo.PlanMMKVRepository
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.common_model.sms.STSTokenModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import com.xiaoxun.apie.common.repo.ExecuteResultDelegate
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class WelcomeRepo(
    private val activity: AppCompatActivity,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: WelcomeViewModel
) : IWelcomeRepo, ExecuteResultDelegate {

    companion object {
        private const val TAG = "WelcomeRepo"
    }

    private val scope = lifecycleOwner.lifecycleScope

    override val disposables: CompositeDisposable = CompositeDisposable()

    private val accountDb: AccountDBRepository by lazy { AccountDBRepository(activity) }

    override fun checkLoginStatus() {
        if (AccountManager.isLogin()) {
            // 异步获取用户信息
            scope.launch {
                AccountMMKVRepository.userId?.let {
                    getUserInfo(it)
                    getSTSToken()
                } ?: let {
                    APieLog.e(
                        TAG,
                        "checkLoginStatus failed: userId 为空，当前线程=${Thread.currentThread().name}"
                    )
                    viewModel.updateLoginStatus(CheckLoginStatus.NotLogin)
                }
            }
        } else {
            APieLog.e(TAG, "checkLoginStatus 失败，当前线程=${Thread.currentThread().name}")
            viewModel.updateLoginStatus(CheckLoginStatus.NotLogin)
        }
    }

    override suspend fun getUserInfo(userId: String) {
        innerGetUserInfo(userId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    APieLog.d(TAG, "当前线程=${Thread.currentThread().name}")
                    saveAccountData2DB(resp)
                    saveAccountData2MMKV(resp)
                    viewModel.updateLoginStatus(CheckLoginStatus.Login)
                } ?: let {
                    APieLog.e(TAG, "getUserInfo failed: data is null")
                    viewModel.updateLoginStatus(CheckLoginStatus.NotLogin)
                }
            },
            onFailure = {
                APieLog.e(TAG, "getUserInfo failed: $it")
                viewModel.updateLoginStatus(CheckLoginStatus.NotLogin)
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

    private suspend fun innerGetUserInfo(userId: String): Result<BaseResponse<AccountModel>> {
        return executeResult {
            DataLoaderManager.instance.getUserInfo(
                QueryUser(userId),
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

    override fun onCleared() {
        disposables.clear()
    }
}