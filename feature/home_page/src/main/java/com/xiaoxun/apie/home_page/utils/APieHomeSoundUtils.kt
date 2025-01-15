package com.xiaoxun.apie.home_page.utils

import com.xiaoxun.apie.common.repo.SettingMMKVRepository
import com.xiaoxun.apie.common.utils.sound_pool.APieSoundPoolHelper

/**
 * 首页音效工具类
 */
object APieHomeSoundUtils {

    /**
     * 根据场景播放音效
     */
    fun playSound(scene: SceneType) {
        when (scene) {
            SceneType.PLAN_DONE_CLICK -> {
                if (getPlanDoneClickSoundStatus()) {
                    val soundInfoId = SettingMMKVRepository.planDoneSoundEffectsId
                    APieSoundPoolHelper.playSoundInfoById(soundInfoId)
                }
            }

            SceneType.PLAN_RESET_CLICK -> {
                if (getPlanResetClickSoundStatus()) {
                    val soundInfoId = SettingMMKVRepository.planResetSoundEffectsId
                    APieSoundPoolHelper.playSoundInfoById(soundInfoId)
                }
            }

            SceneType.PLAN_DELETE_CLICK -> {
                if (getPlanDeleteClickSoundStatus()) {
                    val soundInfoId = SettingMMKVRepository.planDeleteSoundEffectsId
                    APieSoundPoolHelper.playSoundInfoById(soundInfoId)
                }
            }
        }
    }
}

/**
 * 打卡音效开关
 */
private fun getPlanDoneClickSoundStatus(): Boolean {
    return SettingMMKVRepository.planDoneSoundEffectsSwitch
}

/**
 * 撤销打卡音效开关
 */
private fun getPlanResetClickSoundStatus(): Boolean {
    return SettingMMKVRepository.planResetSoundEffectsSwitch
}

/**
 * 删除任务音效开关
 */
private fun getPlanDeleteClickSoundStatus(): Boolean {
    return SettingMMKVRepository.planDeleteSoundEffectsSwitch
}

enum class SceneType {
    PLAN_DONE_CLICK, // 打卡完成点击
    PLAN_RESET_CLICK, // 打卡撤销点击
    PLAN_DELETE_CLICK, // 打卡删除点击
}