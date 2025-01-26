package com.xiaoxun.apie.home_page.repo.desire

import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesire
import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesireRequestBody
import com.xiaoxun.apie.apie_data_loader.request.desire.ExchangeDesire
import com.xiaoxun.apie.apie_data_loader.request.desire.LoadDesire
import com.xiaoxun.apie.apie_data_loader.request.desire.LoadDesireGroupRequest
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import com.xiaoxun.apie.gold_manage.service.GoldService
import com.xiaoxun.apie.common.repo.ExecuteResultDelegate
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupRespModel
import com.xiaoxun.apie.home_page.viewmodel.IndexDesireViewModel
import io.reactivex.disposables.CompositeDisposable

class IndexDesireRepoImpl(
    private val indexDesireViewModel: IndexDesireViewModel,
    private val goldService: GoldService? = null
) : IIndexDesireRepo, ExecuteResultDelegate {

    override val disposables = CompositeDisposable()

    override suspend fun createDesire(createDesireRequestBody: CreateDesireRequestBody) {
        indexDesireViewModel.onCreateDesireStart()
        innerCreateDesire(createDesireRequestBody).fold(
            onSuccess = {
                it.data?.let { resp ->
                    indexDesireViewModel.onCreateDesireSuccess(resp)
                } ?: let {
                    indexDesireViewModel.onCreateDesireFailed("data is null.")
                }
            },
            onFailure = {
                indexDesireViewModel.onCreateDesireFailed(it.message.toString())
            }
        )
    }

    override suspend fun loadDesireList(userId: String?, manualRefresh: Boolean) {
        if (userId.isNullOrEmpty()) {
            indexDesireViewModel.onLoadDesireListFailed("userId is null or empty.")
            return
        }
        if (manualRefresh.not()) {
            indexDesireViewModel.onLoadDesireListStart()
        }
        innerLoadDesireList(userId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    indexDesireViewModel.onLoadDesireListSuccess(resp.desireList.toMutableList())
                } ?: let {
                    indexDesireViewModel.onLoadDesireListFailed("data is null.")
                }
            },
            onFailure = {
                indexDesireViewModel.onLoadDesireListFailed(it.message.toString())
            }
        )
    }

    override suspend fun exchangeDesire(exchangeDesireId: String) {
        indexDesireViewModel.onExchangeDesireStart()
        innerExchangeDesire(exchangeDesireId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    indexDesireViewModel.onExchangeDesireSuccess(resp)
                    goldService?.reduceGold(resp.desirePrice)
                } ?: let {
                    indexDesireViewModel.onExchangeDesireFailed("data is null.")
                }
            },
            onFailure = {
                indexDesireViewModel.onExchangeDesireFailed(it.message.toString())
            }
        )
    }

    override suspend fun loadDesireGroupList(userId: String) {
        indexDesireViewModel.onLoadDesireGroupListStart()
        innerLoadDesireGroupList(userId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    indexDesireViewModel.onLoadDesireGroupListSuccess(resp.desireGroupList.toMutableList())
                } ?: let {
                    indexDesireViewModel.onLoadDesireGroupListFailed("data is null.")
                }
            },
            onFailure = {
                indexDesireViewModel.onLoadDesireGroupListFailed(it.message.toString())
            }
        )
    }

    private suspend fun innerCreateDesire(createDesireRequestBody: CreateDesireRequestBody): Result<BaseResponse<DesireModel>> {
        return executeResult {
            DataLoaderManager.instance.createDesire(
                CreateDesire(createDesireRequestBody),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerLoadDesireList(userId: String): Result<BaseResponse<DesireRespModel>> {
        return executeResult {
            DataLoaderManager.instance.loadDesireListByUserId(
                LoadDesire(userId),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerExchangeDesire(desireId: String): Result<BaseResponse<DesireModel>> {
        return executeResult {
            DataLoaderManager.instance.exchangeDesire(
                ExchangeDesire(desireId),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerLoadDesireGroupList(userId: String): Result<BaseResponse<DesireGroupRespModel>> {
        return executeResult {
            DataLoaderManager.instance.loadDesireGroupListByUserId(
                LoadDesireGroupRequest(userId),
                CacheStrategy.FORCE_NET
            )
        }
    }

    override fun onCleared() {
        disposables.clear()
    }
}