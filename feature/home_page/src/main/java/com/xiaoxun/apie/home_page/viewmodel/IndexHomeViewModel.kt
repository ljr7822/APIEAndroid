package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanRespModel

class IndexHomeViewModel: APieBaseViewModel() {

    private var _listScrolling = MutableLiveData<Boolean>()
    val listScrolling get() = _listScrolling

    private var _loadPlanListState = MutableLiveData<LoadPlanListState>()
    val loadPlanListState get() = _loadPlanListState

    private var _planList = MutableLiveData<List<PlanModel>>()
    val planList get() = _planList

    fun loadPlanListStart() {
        _loadPlanListState.value = LoadPlanListState.START
    }

    fun updateListScrolling(isScrolling: Boolean) {
        if (_listScrolling.value == isScrolling) return
        _listScrolling.value = isScrolling
    }

    fun loadPlanListSuccess(newPlanList: List<PlanModel>) {
        _loadPlanListState.value = LoadPlanListState.SUCCESS
        _planList.value = newPlanList
    }

    fun loadPlanListFailed(error: String) {
        _loadPlanListState.value = LoadPlanListState.FAILED
    }
}

enum class LoadPlanListState {
    START,
    SUCCESS,
    FAILED
}