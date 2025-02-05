package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.desire.group.DesireGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel

class IndexDesireViewModel : APieBaseViewModel() {

    // ********************************************* 状态 *********************************************
    // 列表滚动状态
    private var _listScrolling = MutableLiveData<Boolean>()
    val listScrolling get() = _listScrolling

    // 通用的loading状态
    private var _commonLoadingState = MutableLiveData<CommonLoadingState>()
    val commonLoadingState get() = _commonLoadingState

    // 创建心愿状态
    private var _createDesireState = MutableLiveData<CreateDesireState>()
    val createDesireState get() = _createDesireState

    // 获取心愿列表状态
    private var _loadDesireListState = MutableLiveData<LoadDesireListState>()
    val loadDesireListState get() = _loadDesireListState

    // 加载心愿分组列表状态
    private var _loadDesireGroupListState = MutableLiveData<LoadGroupListState>()
    val loadDesireGroupListState get() = _loadDesireGroupListState

    // ********************************************* 数据 *********************************************
    // 当前显示的心愿列表数据
    private var _currentDesireList = MutableLiveData<MutableList<DesireModel>>()
    val currentDesireList get() = _currentDesireList

    // 心愿分组列表
    private var _desireGroupList = MutableLiveData<MutableList<DesireGroupModel>>()
    val desireGroupList get() = _desireGroupList

    // 记录选择的时间区间
    private var _selectTimeRange = MutableLiveData<HashMap<TimeRangeType, Pair<Long, String>>>()
    val selectTimeRange get() = _selectTimeRange

    // 创建选中的心愿分组
    private var _selectDesireGroup = MutableLiveData<String>()
    val selectDesireGroup get() = _selectDesireGroup

    // ********************************************* 方法 *********************************************
    fun updateListScrolling(isScrolling: Boolean) {
        if (_listScrolling.value == isScrolling) return
        _listScrolling.value = isScrolling
    }

    fun onCreateDesireStart() {
        _createDesireState.value = CreateDesireState.START
        showLoadingStart()
    }

    fun onCreateDesireSuccess(data: DesireModel) {
        _createDesireState.value = CreateDesireState.SUCCESS
        showLoadingSuccess()
        updateOrInsertDesire(data)
    }

    fun onCreateDesireFailed(error: String) {
        _createDesireState.value = CreateDesireState.FAILED
        showLoadingFailed()
    }

    fun onLoadDesireListStart() {
        _loadDesireListState.value = LoadDesireListState.START
        showLoadingStart()
    }

    fun onLoadDesireListSuccess(desireList: MutableList<DesireModel>) {
        _loadDesireListState.value = LoadDesireListState.SUCCESS
        _currentDesireList.value = desireList
        showLoadingSuccess()
    }

    fun onLoadDesireListFailed(error: String) {
        _loadDesireListState.value = LoadDesireListState.FAILED
        showLoadingFailed()
    }

    fun onExchangeDesireStart() {
        showLoadingStart()
    }

    fun onExchangeDesireSuccess(data: DesireModel) {
        showLoadingSuccess()
        updateOrInsertDesire(data)
    }

    fun onExchangeDesireFailed(error: String) {
        showLoadingFailed()
    }

    fun onLoadDesireGroupListStart() {
        _loadDesireGroupListState.value = LoadGroupListState.START
    }

    fun onLoadDesireGroupListSuccess(newPlanGroupList: MutableList<DesireGroupModel>) {
        _loadDesireGroupListState.value = LoadGroupListState.SUCCESS
        _desireGroupList.value = newPlanGroupList
    }

    fun onLoadDesireGroupListFailed(error: String) {
        _loadDesireGroupListState.value = LoadGroupListState.FAILED
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

    fun updateSelectDesireGroup(desireGroupId: String) {
        _selectDesireGroup.value = desireGroupId
    }

    fun createDesireGroupSuccess(groupModel: DesireGroupModel) {
        _commonLoadingState.value = CommonLoadingState.SUCCESS
        insertGroup(groupModel)
    }

    private fun insertGroup(groupModel: DesireGroupModel) {
        val currentList = _desireGroupList.value ?: mutableListOf()
        currentList.add(0, groupModel)
        _desireGroupList.value = currentList
    }

    /**
     * 更新或插入心愿
     */
    private fun updateOrInsertDesire(resp: DesireModel) {
        val currentList = _currentDesireList.value ?: mutableListOf()
        val indexToUpdate = currentList.indexOfFirst { it.desireId == resp.desireId }

        if (indexToUpdate != -1) {
            // 更新现有数据
            currentList[indexToUpdate] = resp
        } else {
            // 插入新数据
            currentList.add(0, resp) // 可按需求改变插入位置，比如插入到列表顶部
        }
        _currentDesireList.value = currentList
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
}