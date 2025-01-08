package com.xiaoxun.apie.gold_manage.service

import androidx.lifecycle.LiveData
import com.xiaoxun.apie.gold_manage.manager.GoldManager

class GoldService {

    // 获取金币 LiveData
    val goldLiveData: LiveData<Int> get() = GoldManager.goldLiveData

    // 初始化金币模块
    fun initializeGoldManager() {
        GoldManager.initialize()
    }

    // 增加金币
    fun increaseGold(goldReward: Int) {
        GoldManager.addGold(goldReward)
    }

    // 减少金币
    fun reduceGold(goldPenalty: Int) {
        GoldManager.subtractGold(goldPenalty)
    }

    // 获取当前金币值
    fun getCurrentGold(): Int {
        return GoldManager.goldLiveData.value ?: 0
    }
}