package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.common_model.home_page.storage.group.ThingGroupModel

class IndexStorageViewModel: APieBaseViewModel() {

    // ********************************************* 状态 *********************************************
    // 通用的loading状态
    private var _commonLoadingState = MutableLiveData<CommonLoadingState>()
    val commonLoadingState get() = _commonLoadingState

    // 加载物品分组列表状态
    private var _loadThingGroupListState = MutableLiveData<LoadGroupListState>()
    val loadThingGroupListState get() = _loadThingGroupListState

    // 获取物品列表状态
    private var _loadThingListState = MutableLiveData<LoadThingListState>()
    val loadThingListState get() = _loadThingListState

    // ********************************************* 数据 *********************************************
    // 事物图标URL
    private val _thingIconUrl: MutableLiveData<String> = MutableLiveData()
    val thingIconUrl: MutableLiveData<String> get() = _thingIconUrl

    // 物品分组列表
    private var _thingGroupList = MutableLiveData<MutableList<ThingGroupModel>>()
    val thingGroupList get() = _thingGroupList

    // 当前显示的物品列表数据
    private var _currentThingList = MutableLiveData<MutableList<ThingItemModel>>()
    val currentThingList get() = _currentThingList

    // ********************************************* 方法 *********************************************
    fun updateThingIconUrl(url: String) {
        _thingIconUrl.value = url
    }

    fun showLoadingStart() {
        _commonLoadingState.value = CommonLoadingState.START
    }

    fun showLoadingSuccess() {
        _commonLoadingState.value = CommonLoadingState.SUCCESS
    }

    fun showLoadingFailed() {
        _commonLoadingState.value = CommonLoadingState.FAILED
    }

    fun onLoadThingGroupListStart() {
        _loadThingGroupListState.value = LoadGroupListState.START
    }

    fun onLoadThingGroupListSuccess(data: MutableList<ThingGroupModel>) {
        _loadThingGroupListState.value = LoadGroupListState.SUCCESS
        _thingGroupList.value = data
    }

    fun onLoadThingGroupListFailed(error: String) {
        _loadThingGroupListState.value = LoadGroupListState.FAILED
    }

    fun onCreateThingGroupSuccess(groupModel: ThingGroupModel) {
        showLoadingSuccess()
        insertGroup(groupModel)
    }

    private fun insertGroup(groupModel: ThingGroupModel) {
        val currentList = _thingGroupList.value ?: mutableListOf()
        currentList.add(0, groupModel)
        _thingGroupList.value = currentList
    }

    fun onLoadThingListStart() {
        _loadThingListState.value = LoadThingListState.START
        showLoadingStart()
    }

    fun onLoadThingListSuccess(desireList: MutableList<ThingItemModel>) {
        _loadThingListState.value = LoadThingListState.SUCCESS
        _currentThingList.value = desireList
        showLoadingSuccess()
    }

    fun onLoadThingListFailed(error: String) {
        _loadThingListState.value = LoadThingListState.FAILED
        showLoadingFailed()
    }
}