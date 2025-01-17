package com.xiaoxun.apie.common.repo

import com.xiaoxun.apie.common.mmkv.MMKVOwner

private const val ACCOUNT_MMKV_NAME = "account_mmkv_store"
object AccountMMKVRepository: MMKVOwner(mmapID = ACCOUNT_MMKV_NAME) {
    var userName by mmkvString("APPLE PIE")
    var userId by mmkvString()
    var userAvatar by mmkvString()
    var userDesc by mmkvString("")
    var token by mmkvString()
    var goldCount by mmkvInt(0)
    var sex by mmkvInt(0)
    var address by mmkvString("")
    var school by mmkvString("")
}

private const val DESIRE_MMKV_NAME = "desire_mmkv_store"
object DesireMMKVRepository: MMKVOwner(mmapID = DESIRE_MMKV_NAME) {
    var desireCount by mmkvInt(0)
}

private const val PLAN_MMKV_NAME = "plan_mmkv_store"
object PlanMMKVRepository: MMKVOwner(mmapID = PLAN_MMKV_NAME) {
    var planCount by mmkvInt(0)
}

private const val SETTING_MMKV_NAME = "setting_mmkv_store"
object SettingMMKVRepository: MMKVOwner(mmapID = SETTING_MMKV_NAME) {
    // 计划完成音效开关
    var planDoneSoundEffectsSwitch by mmkvBool(false)
    // 计划删除音效开关
    var planDeleteSoundEffectsSwitch by mmkvBool(false)
    // 计划重置音效开关
    var planResetSoundEffectsSwitch by mmkvBool(false)

    // 任务完成音效id
    var planDoneSoundEffectsId by mmkvInt(6)
    // 任务删除音效id
    var planDeleteSoundEffectsId by mmkvInt(6)
    // 任务重置音效id
    var planResetSoundEffectsId by mmkvInt(6)
}