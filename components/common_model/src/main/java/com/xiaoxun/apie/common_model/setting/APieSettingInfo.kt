package com.xiaoxun.apie.common_model.setting

import com.xiaoxun.apie.common.utils.sound_pool.SoundInfo


data class APieSettingInfo(
    val settingType: SettingInfoType,
    val itemTitle: String? = null,
    val desc: String? = null,
    val leftIcon: Int? = null,
    val leftText: String,
    val rightText: String? = null,
    val centerText: String? = null,
    val isShowSwitch: Boolean,
    val switchIsCheck: Boolean? = false,
    val isShowArrow: Boolean? = false,
    val isShowTick: Boolean? = false,
    val soundEffectsInfoList: MutableMap<SettingInfoType, MutableList<SoundEffectsInfo>>? = null
)

data class SoundEffectsInfo(
    val soundInfo: SoundInfo
)

enum class SettingInfoType(val type: Int) {
    PLAN_DONE_SOUND_EFFECTS(1), // 计划完成
    PLAN_RESET_SOUND_EFFECTS(2), // 计划撤销
    PLAN_DELETE_SOUND_EFFECTS(3), // 计划删除
}