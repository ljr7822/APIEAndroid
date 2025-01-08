package com.xiaoxun.apie.gold_manage.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper

object GoldManager {
    // 当前金币数的 LiveData
    private val _goldLiveData = MutableLiveData<Int>()
    val goldLiveData: LiveData<Int> get() = _goldLiveData

    // 初始化金币（从持久化加载）
    fun initialize() {
        val gold = SharedPreferencesHelper.getInt("currentGold", 0)
        _goldLiveData.postValue(gold)
    }

    // 增加金币
    fun addGold(amount: Int) {
        val newGold = (_goldLiveData.value ?: 0) + amount
        updateGold(newGold)
    }

    // 减少金币
    fun subtractGold(amount: Int) {
        val newGold = maxOf(0, (_goldLiveData.value ?: 0) - amount) // 防止负值
        updateGold(newGold)
    }

    // 更新金币值并持久化
    private fun updateGold(newGold: Int) {
        _goldLiveData.postValue(newGold)
        SharedPreferencesHelper.putInt("currentGold", newGold)
    }
}