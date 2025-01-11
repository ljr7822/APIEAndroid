package com.xiaoxun.apie.home_page.utils

import com.xiaoxun.apie.common.utils.SharedPreferencesHelper
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
                    val soundInfoId = SharedPreferencesHelper.getInt(
                        SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_CLICK_DONE_ID_KEY, 6
                    )
                    APieSoundPoolHelper.playSoundInfoById(soundInfoId)
                }
            }

            SceneType.PLAN_RESET_CLICK -> {
                if (getPlanResetClickSoundStatus()) {
                    val soundInfoId = SharedPreferencesHelper.getInt(
                        SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_CLICK_RESET_ID_KEY, 6
                    )
                    APieSoundPoolHelper.playSoundInfoById(soundInfoId)
                }
            }

            SceneType.PLAN_DELETE_CLICK -> {
                if (getPlanDeleteClickSoundStatus()) {
                    val soundInfoId = SharedPreferencesHelper.getInt(
                        SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_CLICK_DELETE_ID_KEY, 6
                    )
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
    return SharedPreferencesHelper.getBoolean(
        SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_DONE_PLAN_SWITCH_KEY,
        false
    )
}

/**
 * 撤销打卡音效开关
 */
private fun getPlanResetClickSoundStatus(): Boolean {
    return SharedPreferencesHelper.getBoolean(
        SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_RESET_PLAN_SWITCH_KEY,
        false
    )
}

/**
 * 删除任务音效开关
 */
private fun getPlanDeleteClickSoundStatus(): Boolean {
    return SharedPreferencesHelper.getBoolean(
        SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_DELETE_PLAN_SWITCH_KEY,
        false
    )
}

enum class SceneType {
    PLAN_DONE_CLICK, // 打卡完成点击
    PLAN_RESET_CLICK, // 打卡撤销点击
    PLAN_DELETE_CLICK, // 打卡删除点击
}