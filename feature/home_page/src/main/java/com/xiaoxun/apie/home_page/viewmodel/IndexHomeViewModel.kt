package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel

class IndexHomeViewModel: APieBaseViewModel() {

    private var _listScrolling = MutableLiveData<Boolean>()
    val listScrolling get() = _listScrolling

    private var _loadPlanListState = MutableLiveData<LoadPlanListState>()
    val loadPlanListState get() = _loadPlanListState

    private var _planTypeList = MutableLiveData<Pair<PlanListType, MutableList<PlanModel>>>()
    val planTypeList get() = _planTypeList

    private var _planStatusList = MutableLiveData<Pair<PlanStatus, MutableList<PlanModel>>>()
    val planStatusList get() = _planStatusList

    private var _planStatus = MutableLiveData<PlanStatus>()
    val planStatus get() = _planStatus

    private var _filterPlanType = MutableLiveData<PlanListType>()
    val filterPlanType get() = _filterPlanType

    init {
        _planStatus.value = PlanStatus.DOING
    }

    fun loadPlanListStart() {
        _loadPlanListState.value = LoadPlanListState.START
    }

    fun updateListScrolling(isScrolling: Boolean) {
        if (_listScrolling.value == isScrolling) return
        _listScrolling.value = isScrolling
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
}