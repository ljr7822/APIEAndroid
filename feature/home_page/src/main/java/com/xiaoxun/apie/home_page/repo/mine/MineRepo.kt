package com.xiaoxun.apie.home_page.repo.mine

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.xiaoxun.apie.account_manager.repo.AccountDBRepository
import com.xiaoxun.apie.account_manager.repo.AccountDataDescriptor
import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.user.QueryUser
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.repo.DesireMMKVRepository
import com.xiaoxun.apie.common.repo.ExecuteResultDelegate
import com.xiaoxun.apie.common.repo.PlanMMKVRepository
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.account.AccountModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import com.xiaoxun.apie.home_page.viewmodel.IndexMineViewModel
import io.reactivex.disposables.CompositeDisposable

class MineRepo(
    private val activity: AppCompatActivity,
    private val viewModel: IndexMineViewModel
) : IMineRepo, ExecuteResultDelegate {

    companion object {
        private const val TAG = "MineRepoImpl"
    }

    override val disposables: CompositeDisposable = CompositeDisposable()

    private val accountDb: AccountDBRepository by lazy { AccountDBRepository(activity) }

    override suspend fun loadUserInfo() {
        viewModel.onLoadInfoStart()
        AccountMMKVRepository.userId?.let {
            innerGetUserInfo(it).fold(
                onSuccess = { response ->
                    if (response.isSuccess()) {
                        response.data?.let { accountModel ->
                            // 更新用户信息
                            APieLog.d(TAG, "当前线程=${Thread.currentThread().name}")
                            saveAccountData2DB(accountModel)
                            saveAccountData2MMKV(accountModel)
                            viewModel.onLoadInfoSuccess(accountModel)
                        }
                    }
                },
                onFailure = { error ->
                    // 处理错误
                    APieLog.d(TAG, "当前线程=${Thread.currentThread().name}, msg=${error.message}")
                    APieToast.showDialog("获取用户信息失败，请重试")
                    viewModel.onLoadInfoFailed()
                }
            )
        } ?: run {
            APieLog.d(TAG, "userId为空")
            APieToast.showDialog("userId为空")
            viewModel.onLoadInfoFailed()
        }
    }

    override fun onCleared() {
        disposables.clear()
    }

    private suspend fun innerGetUserInfo(userId: String): Result<BaseResponse<AccountModel>> {
        return executeResult {
            DataLoaderManager.instance.getUserInfo(
                QueryUser(userId),
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
        PlanMMKVRepository.planCount = accountModel.totalPlan
        DesireMMKVRepository.desireCount = accountModel.totalDesire
    }
}