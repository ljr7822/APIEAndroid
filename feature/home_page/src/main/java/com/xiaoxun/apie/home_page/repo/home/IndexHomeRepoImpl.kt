package com.xiaoxun.apie.home_page.repo.home

import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlan
import com.xiaoxun.apie.apie_data_loader.request.group.CreatePlanGroup
import com.xiaoxun.apie.apie_data_loader.request.group.CreatePlanGroupRequestBody
import com.xiaoxun.apie.apie_data_loader.request.group.DeleteGroup
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.apie_data_loader.request.plan.DeletePlan
import com.xiaoxun.apie.apie_data_loader.request.group.LoadPlanGroups
import com.xiaoxun.apie.apie_data_loader.request.plan.LoadPlans
import com.xiaoxun.apie.apie_data_loader.request.group.UpdateGroupRequestBody
import com.xiaoxun.apie.apie_data_loader.request.plan.UpdatePlanCompletedCount
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common_model.home_page.group.DeleteGroupRespModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupRespModel
import com.xiaoxun.apie.common_model.home_page.plan.DeletePlanRespModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import com.xiaoxun.apie.gold_manage.service.GoldService
import com.xiaoxun.apie.common.repo.ExecuteResultDelegate
import com.xiaoxun.apie.common_model.view_model.CompletedCountOptType
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.common_model.view_model.PlanListType
import com.xiaoxun.apie.common_model.view_model.PlanStatus
import io.reactivex.disposables.CompositeDisposable

class IndexHomeRepoImpl(
    private val viewModel: IndexHomeViewModel,
    private val goldService: GoldService? = null
) :
    IIndexHomeRepo, ExecuteResultDelegate {
    companion object {
        private const val TAG = "IndexHomeRepo"
    }

    override val disposables = CompositeDisposable()

    override suspend fun loadPlanByType(planType: PlanListType, manualRefresh: Boolean) {
        if (manualRefresh.not()) {
            viewModel.loadPlanListStart()
        }
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
        viewModel.loadPlanListStart()
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
        // TODO: xiaoxun 待实现
    }

    override suspend fun createPlan(createPlanRequestBody: CreatePlanRequestBody) {
        viewModel.createPlanStart()
        innerCreatePlan(createPlanRequestBody).fold(
            onSuccess = {
                it.data?.let { resp ->
                    viewModel.createPlanSuccess(resp)
                } ?: let {
                    viewModel.createPlanFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.createPlanFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun loadPlanGroupList() {
        viewModel.loadPlanGroupListStart()
        innerLoadPlanGroupList().fold(
            onSuccess = {
                it.data?.let { resp ->
                    viewModel.loadPlanGroupListSuccess(resp.planGroupList.toMutableList())
                } ?: let {
                    viewModel.loadPlanGroupListFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.loadPlanGroupListFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun createPlanGroup(groupName: String) {
        viewModel.createPlanGroupStart()
        innerCreatePlanGroup(groupName).fold(
            onSuccess = {
                it.data?.let { resp ->
                    viewModel.createPlanGroupSuccess(resp)
                } ?: let {
                    viewModel.createPlanGroupFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.createPlanGroupFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun updatePlanCompletedCount(optType: CompletedCountOptType, planId: String) {
        innerUpdatePlanCompletedCount(optType.type, planId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    viewModel.updatePlanCompletedCountSuccess(optType, resp)
                    // 调整全局金币数量
                    if (optType == CompletedCountOptType.INCREMENT) {
                        goldService?.increaseGold(resp.planAward)
                    } else {
                        goldService?.reduceGold(resp.planAward)
                    }
                } ?: let {
                    viewModel.updatePlanCompletedCountFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.updatePlanCompletedCountFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun deletePlan(planId: String) {
        innerDeletePlan(planId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    viewModel.deletePlanSuccess(resp.planId)
                } ?: let {
                    viewModel.deletePlanFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.deletePlanFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun deleteGroup(groupId: String) {
        viewModel.deleteGroupStart()
        innerDeleteGroup(groupId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    viewModel.deleteGroupSuccess(resp.groupId)
                } ?: let {
                    viewModel.deleteGroupFailed("data is null.")
                }
            },
            onFailure = {
                viewModel.deleteGroupFailed(it.localizedMessage ?: "request error.")
            }
        )
    }

    override suspend fun updateGroup(updateGroupRequestBody: UpdateGroupRequestBody) {
        TODO("Not yet implemented")
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

    private suspend fun innerLoadPlanGroupList(): Result<BaseResponse<PlanGroupRespModel>> {
        return executeResult {
            DataLoaderManager.instance.getAllPlanGroupByUserId(
                LoadPlanGroups(AccountManager.getUserId()),
                CacheStrategy.FIRST_CACHE_AND_SEND_REQUEST
            )
        }
    }

    private suspend fun innerCreatePlanGroup(groupName: String): Result<BaseResponse<PlanGroupModel>> {
        return executeResult {
            DataLoaderManager.instance.createPlanGroup(
                CreatePlanGroup(
                    CreatePlanGroupRequestBody(
                        groupName = groupName,
                        createUserId = AccountManager.getUserId()
                    )
                ),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerCreatePlan(createPlanRequestBody: CreatePlanRequestBody): Result<BaseResponse<PlanModel>> {
        return executeResult {
            DataLoaderManager.instance.createPlan(
                CreatePlan(createPlanRequestBody),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerUpdatePlanCompletedCount(
        optType: Int,
        planId: String
    ): Result<BaseResponse<PlanModel>> {
        return executeResult {
            DataLoaderManager.instance.updatePlanCompletedCount(
                UpdatePlanCompletedCount(optType, planId),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerDeletePlan(planId: String): Result<BaseResponse<DeletePlanRespModel>> {
        return executeResult {
            DataLoaderManager.instance.deletePlan(
                DeletePlan(planId),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerDeleteGroup(groupId: String): Result<BaseResponse<DeleteGroupRespModel>> {
        return executeResult {
            DataLoaderManager.instance.deleteGroup(
                DeleteGroup(groupId),
                CacheStrategy.FORCE_NET
            )
        }
    }
}