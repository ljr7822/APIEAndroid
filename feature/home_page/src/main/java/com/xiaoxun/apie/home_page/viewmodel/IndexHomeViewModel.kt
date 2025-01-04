package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel

class IndexHomeViewModel: APieBaseViewModel() {

    private var _listScrolling = MutableLiveData<Boolean>()
    val listScrolling get() = _listScrolling

    private var _loadPlanListState = MutableLiveData<LoadPlanListState>()
    val loadPlanListState get() = _loadPlanListState

    private var _loadPlanGroupListState = MutableLiveData<LoadPlanGroupListState>()
    val loadPlanGroupListState get() = _loadPlanGroupListState

    private var _planGroupList = MutableLiveData<MutableList<PlanGroupModel>>()
    val planGroupList get() = _planGroupList

    private var _planTypeList = MutableLiveData<Pair<PlanListType, MutableList<PlanModel>>>()
    val planTypeList get() = _planTypeList

    private var _planStatusList = MutableLiveData<Pair<PlanStatus, MutableList<PlanModel>>>()
    val planStatusList get() = _planStatusList

    private var _planStatus = MutableLiveData<PlanStatus>()
    val planStatus get() = _planStatus

    private var _filterPlanType = MutableLiveData<PlanListType>()
    val filterPlanType get() = _filterPlanType

    private var _createPlanState = MutableLiveData<CreatePlanState>()
    val createPlanState get() = _createPlanState

    init {
        _filterPlanType.value = PlanListType.ALL_PLAN
    }

    fun loadPlanListStart() {
        _loadPlanListState.value = LoadPlanListState.START
    }

    fun createPlanStart() {
        _createPlanState.value = CreatePlanState.START
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

    fun loadPlanGroupListStart() {
        _loadPlanGroupListState.value = LoadPlanGroupListState.START
    }

    fun loadPlanGroupListSuccess(newPlanGroupList: MutableList<PlanGroupModel>) {
        _loadPlanGroupListState.value = LoadPlanGroupListState.SUCCESS
        _planGroupList.value = newPlanGroupList
    }

    fun loadPlanGroupListFailed(error: String) {
        _loadPlanGroupListState.value = LoadPlanGroupListState.FAILED
    }

    fun createPlanSuccess(plan: PlanModel) {
        _createPlanState.value = CreatePlanState.SUCCESS
    }

    fun createPlanFailed(error: String) {
        _createPlanState.value = CreatePlanState.FAILED
    }


    fun updateListScrolling(isScrolling: Boolean) {
        if (_listScrolling.value == isScrolling) return
        _listScrolling.value = isScrolling
    }

}