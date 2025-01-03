package com.xiaoxun.apie.home_page.repo

import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.account.plan.LoadPlans
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.account.AccountManager
import com.xiaoxun.apie.common.utils.coroutine.singleSuspendCoroutine
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.PlanListType
import com.xiaoxun.apie.home_page.viewmodel.PlanStatus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.resume

class IndexHomeRepo(private val viewModel: IndexHomeViewModel) : IIndexHomeRepo {
    companion object {
        private const val TAG = "IndexHomeRepo"
    }

    private val disposables = CompositeDisposable()

    override suspend fun loadPlanByType(planType: PlanListType) {
        innerLoadPlanList().fold(
            onSuccess = {
                it.data?.let { resp ->
                    val resultList = filterResultList(planType, resp.planList)
                    viewModel.loadPlanByTypeSuccess(Pair(planType, resultList.toMutableList()))
                } ?: let {
                    viewModel.loadPlanListFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.loadPlanListFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun loadPlanByStatus(status: PlanStatus) {
        innerLoadPlanList().fold(
            onSuccess = {
                it.data?.let { resp ->
                    val resultList = resp.planList.filter { it.planStatus == status.status }
                    viewModel.loadPlanByStatusSuccess(Pair(status, resultList.toMutableList()))
                } ?: let {
                    viewModel.loadPlanListFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.loadPlanListFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun loadPlanDetail(planId: String) {

    }

    override suspend fun createPlan() {

    }

    /**
     * 在生命周期结束时清理订阅
     */
    override fun onCleared() {
        disposables.clear()
    }

    private fun filterResultList(
        planType: PlanListType,
        planList: List<PlanModel>
    ): List<PlanModel> {
        APieLog.i(TAG, "planList: $planList")
        return if (planType != PlanListType.ALL_PLAN) {
            planList.filter { it.planType == planType.type }
        } else {
            planList
        }
    }

    private suspend fun innerLoadPlanList(): Result<BaseResponse<PlanRespModel>> {
        return executeResult {
            DataLoaderManager.instance.getAllPlanByUserId(
                LoadPlans(AccountManager.getUserId()),
                CacheStrategy.FIRST_CACHE_AND_SEND_REQUEST
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