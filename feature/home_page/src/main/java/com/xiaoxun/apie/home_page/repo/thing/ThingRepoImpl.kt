package com.xiaoxun.apie.home_page.repo.thing

import com.xiaoxun.apie.apie_data_loader.DataLoaderManager
import com.xiaoxun.apie.apie_data_loader.request.thing.CreateThing
import com.xiaoxun.apie.apie_data_loader.request.thing.CreateThingGroup
import com.xiaoxun.apie.apie_data_loader.request.thing.CreateThingGroupRequestBody
import com.xiaoxun.apie.apie_data_loader.request.thing.CreateThingRequestBody
import com.xiaoxun.apie.apie_data_loader.request.thing.DeleteThing
import com.xiaoxun.apie.apie_data_loader.request.thing.LoadThingGroupRequest
import com.xiaoxun.apie.apie_data_loader.request.thing.LoadThings
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common.repo.ExecuteResultDelegate
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingRespModel
import com.xiaoxun.apie.common_model.home_page.storage.group.ThingGroupModel
import com.xiaoxun.apie.common_model.home_page.storage.group.ThingGroupRespModel
import com.xiaoxun.apie.common_model.home_page.thing.CreateThingInfo
import com.xiaoxun.apie.common_model.home_page.thing.DeleteThingRespModel
import com.xiaoxun.apie.data_loader.data.BaseResponse
import com.xiaoxun.apie.data_loader.utils.CacheStrategy
import com.xiaoxun.apie.home_page.viewmodel.IndexStorageViewModel
import io.reactivex.disposables.CompositeDisposable

class ThingRepoImpl(
    private val thingViewModel: IndexStorageViewModel
) : IThingRepo, ExecuteResultDelegate {
    override val disposables: CompositeDisposable = CompositeDisposable()

    override suspend fun loadThingGroups(source: ThingGroupSource) {
        thingViewModel.onLoadThingGroupListStart()
        innerLoadThingGroups().fold(
            onSuccess = {
                it.data?.let { resp ->
                    val newGroupList = mutableListOf<ThingGroupModel>()
                    newGroupList.addAll(resp.thingGroupList)
                    thingViewModel.onLoadThingGroupListSuccess(newGroupList)
                } ?: let {
                    thingViewModel.onLoadThingGroupListFailed("data is null.")
                }
            },
            onFailure = {
                thingViewModel.onLoadThingGroupListFailed(it.message.toString())
            }
        )
    }

    override suspend fun loadThingList() {
        thingViewModel.onLoadThingListStart()
        innerLoadThingList().fold(
            onSuccess = {
                it.data?.let { resp ->
                    thingViewModel.onLoadThingListSuccess(resp.thingList.toMutableList())
                } ?: let {
                    thingViewModel.onLoadThingListFailed("data is null.")
                }
            },
            onFailure = {
                thingViewModel.onLoadThingListFailed(it.message.toString())
            }
        )
    }

    override suspend fun createThingGroup(groupName: String) {
        thingViewModel.showLoadingStart()
        innerCreateThingGroup(groupName).fold(
            onSuccess = {
                it.data?.let { resp ->
                    thingViewModel.onCreateThingGroupSuccess(resp)
                } ?: let {
                    thingViewModel.showLoadingFailed()
                }
            },
            onFailure = {
                thingViewModel.showLoadingFailed()
            }
        )
    }

    override suspend fun createThing(createThingInfo: CreateThingInfo) {
        thingViewModel.onCreateThingStart()
        innerCreateThing(createThingInfo).fold(
            onSuccess = {
                it.data?.let { resp ->
                    thingViewModel.onCreateThingSuccess(resp)
                } ?: let {
                    thingViewModel.onCreateThingFailed("data is null.")
                }
            },
            onFailure = {
                thingViewModel.onCreateThingFailed(it.message.toString())
            }
        )
    }

    override suspend fun deleteThing(thingId: String) {
        thingViewModel.showLoadingStart()
        innerDeleteThing(thingId).fold(
            onSuccess = {
                it.data?.let { resp ->
                    thingViewModel.deleteThingSuccess(resp.thingId)
                }
            },
            onFailure = {
                thingViewModel.deletePlanFailed(it.message.toString())
            }
        )
    }

    private suspend fun innerLoadThingGroups(): Result<BaseResponse<ThingGroupRespModel>> {
        return executeResult {
            DataLoaderManager.instance.loadThingGroups(
                LoadThingGroupRequest(AccountManager.getUserId()),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerLoadThingList(): Result<BaseResponse<ThingRespModel>> {
        return executeResult {
            DataLoaderManager.instance.loadThingList(
                LoadThings(AccountManager.getUserId()),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerCreateThingGroup(groupName: String): Result<BaseResponse<ThingGroupModel>> {
        return executeResult {
            DataLoaderManager.instance.createThingGroup(
                CreateThingGroup(
                    CreateThingGroupRequestBody(
                        thingGroupName = groupName,
                        createUserId = AccountManager.getUserId()
                    )
                ),
                CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerCreateThing(createThingInfo: CreateThingInfo): Result<BaseResponse<ThingItemModel>> {
        return executeResult {
            DataLoaderManager.instance.createThing(
                CreateThing(
                    CreateThingRequestBody(
                        thingName = createThingInfo.thingName,
                        createUserId = AccountManager.getUserId(),
                        belongGroupId = createThingInfo.belongGroupId,
                        thingPrice = createThingInfo.thingPrice,
                        thingTags = createThingInfo.thingTags,
                        thingIcon = createThingInfo.thingIcon,
                        thingStatus = createThingInfo.thingStatus,
                        thingDesc = createThingInfo.thingDesc,
                        buyAt = createThingInfo.buyAt,
                        warrantyPeriod = createThingInfo.warrantyPeriod,
                        thingAppendixList = createThingInfo.thingAppendixList
                    )
                ), CacheStrategy.FORCE_NET
            )
        }
    }

    private suspend fun innerDeleteThing(thingId: String): Result<BaseResponse<DeleteThingRespModel>> {
        return executeResult {
            DataLoaderManager.instance.deleteThing(
                DeleteThing(thingId),
                CacheStrategy.FORCE_NET
            )
        }
    }
}