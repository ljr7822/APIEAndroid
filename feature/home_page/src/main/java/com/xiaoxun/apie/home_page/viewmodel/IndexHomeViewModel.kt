package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel

class IndexHomeViewModel : APieBaseViewModel() {

    // 列表滚动状态
    private var _listScrolling = MutableLiveData<Boolean>()
    val listScrolling get() = _listScrolling

    // 加载计划列表状态
    private var _loadPlanListState = MutableLiveData<LoadPlanListState>()
    val loadPlanListState get() = _loadPlanListState

    // 加载计划分组列表状态
    private var _loadPlanGroupListState = MutableLiveData<LoadGroupListState>()
    val loadPlanGroupListState get() = _loadPlanGroupListState

    // 创建计划分状态
    private var _createPlanGroupState = MutableLiveData<CreatePlanGroupState>()
    val createPlanGroupState get() = _createPlanGroupState

    // 删除计划分状态
    private var _deletePlanGroupState = MutableLiveData<DeletePlanGroupState>()
    val deletePlanGroupState get() = _deletePlanGroupState

    // 计划分组列表
    private var _planGroupList = MutableLiveData<MutableList<PlanGroupModel>>()
    val planGroupList get() = _planGroupList

    // 计划列表: 计划类型-计划列表
    private var _planTypeList = MutableLiveData<Pair<PlanListType, MutableList<PlanModel>>>()
    val planTypeList get() = _planTypeList

    // 计划列表: 计划状态-计划列表
    private var _planStatusList = MutableLiveData<Pair<PlanStatus, MutableList<PlanModel>>>()
    val planStatusList get() = _planStatusList

    // 当前显示的列表数据
    private var _currentPlanList = MutableLiveData<MutableList<PlanModel>>()
    val currentPlanList get() = _currentPlanList

    // 计划状态
    private var _planStatus = MutableLiveData<PlanStatus>()
    val planStatus get() = _planStatus

    // 筛选计划类型
    private var _filterPlanType = MutableLiveData<PlanListType>()
    val filterPlanType get() = _filterPlanType

    // 创建计划状态
    private var _createPlanState = MutableLiveData<CreatePlanState>()
    val createPlanState get() = _createPlanState

    // 创建事选中的计划频率
    private var _selectPlanType = MutableLiveData<PlanListType>()
    val selectPlanType get() = _selectPlanType

    // 创建选中的计划分组
    private var _selectPlanGroup = MutableLiveData<String>()
    val selectPlanGroup get() = _selectPlanGroup

    // 记录选择的时间区间
    private var _selectTimeRange = MutableLiveData<HashMap<TimeRangeType, Pair<Long, String>>>()
    val selectTimeRange get() = _selectTimeRange

    init {
        _filterPlanType.value = PlanListType.ALL_PLAN
        _createPlanState.value = CreatePlanState.INIT_STATUS
        _selectPlanType.value = PlanListType.INIT_TYPE
    }

    fun loadPlanListStart() {
        _loadPlanListState.value = LoadPlanListState.START
    }

    fun createPlanStart() {
        _createPlanState.value = CreatePlanState.START
    }

    fun resetCreatePlanStatus() {
        _createPlanState.value = CreatePlanState.INIT_STATUS
    }

    fun loadPlanByTypeSuccess(newPlanListAndType: Pair<PlanListType, MutableList<PlanModel>>) {
        _loadPlanListState.value = LoadPlanListState.SUCCESS
        _planTypeList.value = newPlanListAndType
    }

    fun loadPlanByStatusSuccess(newPlanListAndType: Pair<PlanStatus, MutableList<PlanModel>>) {
        _loadPlanListState.value = LoadPlanListState.SUCCESS
        _planStatusList.value = newPlanListAndType
    }

    fun loadPlanListFailed(error: String) {
        _loadPlanListState.value = LoadPlanListState.FAILED
    }

    fun updateCurrentPlanList(planList: MutableList<PlanModel>) {
        _currentPlanList.value = planList
    }

    fun loadPlanGroupListStart() {
        _loadPlanGroupListState.value = LoadGroupListState.START
    }

    fun loadPlanGroupListSuccess(newPlanGroupList: MutableList<PlanGroupModel>) {
        _loadPlanGroupListState.value = LoadGroupListState.SUCCESS
        _planGroupList.value = newPlanGroupList
    }

    fun loadPlanGroupListFailed(error: String) {
        _loadPlanGroupListState.value = LoadGroupListState.FAILED
    }

    fun createPlanGroupStart() {
        _createPlanGroupState.value = CreatePlanGroupState.START
    }

    fun createPlanGroupSuccess(groupModel: PlanGroupModel) {
        _createPlanGroupState.value = CreatePlanGroupState.SUCCESS
        insertGroup(groupModel)
    }

    fun createPlanGroupFailed(error: String) {
        _createPlanGroupState.value = CreatePlanGroupState.FAILED
    }

    fun createPlanSuccess(plan: PlanModel) {
        _createPlanState.value = CreatePlanState.SUCCESS
        updateOrInsertPlan(plan)
    }

    fun createPlanFailed(error: String) {
        _createPlanState.value = CreatePlanState.FAILED
    }

    fun updateSelectPlanFrequency(planListType: PlanListType) {
        _selectPlanType.value = planListType
    }

    fun updateSelectPlanGroup(planGroupId: String) {
        _selectPlanGroup.value = planGroupId
    }

    fun updateListScrolling(isScrolling: Boolean) {
        if (_listScrolling.value == isScrolling) return
        _listScrolling.value = isScrolling
    }

    private fun insertGroup(groupModel: PlanGroupModel) {
        val currentList = _planGroupList.value ?: mutableListOf()
        currentList.add(0, groupModel)
        _planGroupList.value = currentList
    }

    /**
     * 更新或插入计划
     */
    private fun updateOrInsertPlan(resp: PlanModel) {
        val currentList = _currentPlanList.value ?: mutableListOf()
        val indexToUpdate = currentList.indexOfFirst { it.planId == resp.planId }

        if (indexToUpdate != -1) {
            // 更新现有数据
            currentList[indexToUpdate] = resp
        } else {
            // 插入新数据
            currentList.add(0, resp) // 可按需求改变插入位置，比如插入到列表顶部
        }

        _currentPlanList.value = currentList
    }

    /**
     * 通过planId移除一个计划
     */
    private fun removePlan(planId: String) {
        val currentList = _currentPlanList.value ?: mutableListOf()
        val removePlan = currentList.find { it.planId == planId }
        if (removePlan != null) {
            currentList.remove(removePlan)
        }
        _currentPlanList.value = currentList
    }

    fun deletePlanSuccess(planId: String) {
        removePlan(planId)
    }

    fun deletePlanFailed(error: String) {

    }

    fun updatePlanCompletedCountSuccess(optType: CompletedCountOptType, resp: PlanModel) {
        updateOrInsertPlan(resp)
    }

    fun updatePlanCompletedCountFailed(error: String) {

    }

    fun getSelectPlanFrequency(): PlanListType {
        return _selectPlanType.value ?: PlanListType.INIT_TYPE
    }

    fun deleteGroupStart() {
        _deletePlanGroupState.value = DeletePlanGroupState.START
    }

    fun deleteGroupSuccess(groupId: String) {
        _deletePlanGroupState.value = DeletePlanGroupState.SUCCESS
        removeGroup(groupId)
    }

    fun deleteGroupFailed(error: String) {
        _deletePlanGroupState.value = DeletePlanGroupState.FAILED
    }

    /**
     * 通过groupId移除一个计划
     */
    private fun removeGroup(groupId: String) {
        val currentGroupList = _planGroupList.value ?: mutableListOf()
        val removeGroup = currentGroupList.find { it.groupId == groupId }
        if (removeGroup != null) {
            currentGroupList.remove(removeGroup)
        }
        _planGroupList.value = currentGroupList
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

    fun selectPlanTypeIsInit(): Boolean {
        return _selectPlanType.value == PlanListType.INIT_TYPE
    }
}