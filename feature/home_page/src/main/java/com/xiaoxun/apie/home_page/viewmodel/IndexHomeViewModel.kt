package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel

class IndexHomeViewModel: APieBaseViewModel() {

    private var _listScrolling = MutableLiveData<Boolean>()
    val listScrolling get() = _listScrolling

    private var _loadPlanListState = MutableLiveData<LoadPlanListState>()
    val loadPlanListState get() = _loadPlanListState

    private var _planList = MutableLiveData<Pair<PlanListType, MutableList<PlanModel>>>()
    val planList get() = _planList

    private var _filterStatus = MutableLiveData<FilterStatus>()
    val filterStatus get() = _filterStatus

    private var _filterPlanType = MutableLiveData<PlanListType>()
    val filterPlanType get() = _filterPlanType

    init {
        _filterStatus.value = FilterStatus.ALL
    }

    fun loadPlanListStart() {
        _loadPlanListState.value = LoadPlanListState.START
    }

    fun updateListScrolling(isScrolling: Boolean) {
        if (_listScrolling.value == isScrolling) return
        _listScrolling.value = isScrolling
    }

    fun loadPlanListSuccess(newPlanListAndType: Pair<PlanListType, MutableList<PlanModel>>) {
        _loadPlanListState.value = LoadPlanListState.SUCCESS
        _planList.value = newPlanListAndType
    }

    fun loadPlanListFailed(error: String) {
        _loadPlanListState.value = LoadPlanListState.FAILED
    }
}