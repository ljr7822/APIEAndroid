package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.desire.DesireRespModel

class IndexDesireViewModel : APieBaseViewModel() {

    // ********************************************* 状态 *********************************************
    // 列表滚动状态
    private var _listScrolling = MutableLiveData<Boolean>()
    val listScrolling get() = _listScrolling

    // 创建心愿状态
    private var _createDesireState = MutableLiveData<CreateDesireState>()
    val createDesireState get() = _createDesireState

    // 获取心愿列表状态
    private var _loadDesireListState = MutableLiveData<LoadDesireListState>()
    val loadDesireListState get() = _loadDesireListState

    // ********************************************* 数据 *********************************************
    // 当前显示的心愿列表数据
    private var _currentDesireList = MutableLiveData<MutableList<DesireModel>>()
    val currentDesireList get() = _currentDesireList

    fun updateListScrolling(isScrolling: Boolean) {
        if (_listScrolling.value == isScrolling) return
        _listScrolling.value = isScrolling
    }

    fun onCreateDesireStart() {
        _createDesireState.value = CreateDesireState.START
    }

    fun onCreateDesireSuccess(data: DesireModel) {
        _createDesireState.value = CreateDesireState.SUCCESS
        updateOrInsertDesire(data)
    }

    fun onCreateDesireFailed(error: String) {
        _createDesireState.value = CreateDesireState.FAILED
    }

    fun onLoadDesireListStart() {
        _loadDesireListState.value = LoadDesireListState.START
    }

    fun onLoadDesireListSuccess(desireList: MutableList<DesireModel>) {
        _loadDesireListState.value = LoadDesireListState.SUCCESS
        _currentDesireList.value = desireList
    }

    fun onLoadDesireListFailed(error: String) {
        _loadDesireListState.value = LoadDesireListState.FAILED
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
}