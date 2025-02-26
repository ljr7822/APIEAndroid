package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.base.IBaseGroupModel
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

    // 创建物品状态
    private var _createThingState = MutableLiveData<CreateThingState>()
    val createThingState get() = _createThingState

    // 当前是什么入口添加图片
    private var _currentAddImageSource = MutableLiveData<AddImageSource>()
    val currentAddImageSource get() = _currentAddImageSource

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

    // 当前选中的分组
    private var _selectThingGroup = MutableLiveData<IBaseGroupModel>()
    val selectThingGroup get() = _selectThingGroup

    // 记录选择的时间区间
    private var _selectTimeRange = MutableLiveData<HashMap<TimeRangeType, Pair<Long, String>>>()
    val selectTimeRange get() = _selectTimeRange

    // 物品附件URL列表
    private val _thingAppendixUrls: MutableLiveData<MutableList<String>> = MutableLiveData()
    val thingAppendixUrls: MutableLiveData<MutableList<String>> get() = _thingAppendixUrls

    // ********************************************* 初始化 *********************************************
    init {
        _currentAddImageSource.value = AddImageSource.THING_ICON
    }

    // ********************************************* 方法 *********************************************
    fun updateCurrentAddImageSource(source: AddImageSource) {
        if (_currentAddImageSource.value == source) {
            return
        }
        _currentAddImageSource.value = source
    }

    fun updateThingIconUrl(url: String) {
        _thingIconUrl.value = url
    }

    fun addThingAppendixUrl(url: String) {
        val currentList = _thingAppendixUrls.value ?: mutableListOf()
        currentList.add(url)
        _thingAppendixUrls.value = currentList
    }

    fun removeThingAppendixUrl(url: String) {
        val currentList = _thingAppendixUrls.value ?: mutableListOf()
        currentList.remove(url)
        _thingAppendixUrls.value = currentList
    }

    fun updateSelectThingGroup(group: IBaseGroupModel) {
        _selectThingGroup.value = group
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

    fun onCreateThingStart() {
        _createThingState.value = CreateThingState.START
    }

    fun onCreateThingSuccess(thingItemModel: ThingItemModel) {
        _createThingState.value = CreateThingState.SUCCESS
        updateOrInsertThing(thingItemModel)
    }

    fun onCreateThingFailed(error: String) {
        _createThingState.value = CreateThingState.FAILED
    }

    /**
     * 更新或插入心愿
     */
    private fun updateOrInsertThing(thingItemModel: ThingItemModel) {
        val currentList = _currentThingList.value ?: mutableListOf()
        val indexToUpdate = currentList.indexOfFirst { it.thingId == thingItemModel.thingId }

        if (indexToUpdate != -1) {
            // 更新现有数据
            currentList[indexToUpdate] = thingItemModel
        } else {
            // 插入新数据
            currentList.add(0, thingItemModel) // 可按需求改变插入位置，比如插入到列表顶部
        }
        _currentThingList.value = currentList
    }

    fun updateSelectTimeRange(timeRangeType: TimeRangeType, timeRange: Pair<Long, String>) {
        val timeRangeMap = _selectTimeRange.value ?: hashMapOf()
        timeRangeMap[timeRangeType] = timeRange
        _selectTimeRange.value = timeRangeMap
    }

    fun getSelectStopTime(): Long? {
        val timeRangeMap = _selectTimeRange.value ?: hashMapOf()
        return timeRangeMap[TimeRangeType.STOP_TIME]?.first
    }

    fun getSelectStartTime(): Long? {
        val timeRangeMap = _selectTimeRange.value ?: hashMapOf()
        return timeRangeMap[TimeRangeType.START_TIME]?.first
    }

    fun isAddThingIcon(): Boolean {
        return _currentAddImageSource.value == AddImageSource.THING_ICON
    }

    fun thingAppendixUrlSize(): Int {
        return _thingAppendixUrls.value?.size ?: 0
    }
}