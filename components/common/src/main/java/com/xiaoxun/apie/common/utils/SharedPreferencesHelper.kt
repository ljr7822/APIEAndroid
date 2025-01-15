package com.xiaoxun.apie.common.utils

import android.content.Context
import android.content.SharedPreferences

@Deprecated("Use MMKV instead")
object SharedPreferencesHelper {

    private const val PREF_NAME = "APIE_SHARED_PREFERENCES"
    private lateinit var sharedPreferences: SharedPreferences
    // 打卡
    const val SP_ACCOUNT_SOUND_EFFECTS_DONE_PLAN_SWITCH_KEY = "sp_account_sound_effects_done_plan_switch_key"
    const val SP_ACCOUNT_SOUND_EFFECTS_CLICK_DONE_ID_KEY = "sp_account_sound_effects_click_done_id_key"

    // 撤销打卡
    const val SP_ACCOUNT_SOUND_EFFECTS_RESET_PLAN_SWITCH_KEY = "sp_account_sound_effects_reset_plan_switch_key"
    const val SP_ACCOUNT_SOUND_EFFECTS_CLICK_RESET_ID_KEY = "sp_account_sound_effects_click_reset_id_key"

    // 删除任务
    const val SP_ACCOUNT_SOUND_EFFECTS_DELETE_PLAN_SWITCH_KEY = "sp_account_sound_effects_delete_plan_switch_key"
    const val SP_ACCOUNT_SOUND_EFFECTS_CLICK_DELETE_ID_KEY = "sp_account_sound_effects_click_delete_id_key"


    /**
     * 初始化方法，应在 Application 类中调用
     */
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * 保存 String 类型数据
     */
    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * 获取 String 类型数据
     */
    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    /**
     * 保存 Int 类型数据
     */
    fun putInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    /**
     * 获取 Int 类型数据
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    /**
     * 保存 Boolean 类型数据
     */
    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    /**
     * 获取 Boolean 类型数据
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    /**
     * 保存 Float 类型数据
     */
    fun putFloat(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    /**
     * 获取 Float 类型数据
     */
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    /**
     * 保存 Long 类型数据
     */
    fun putLong(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    /**
     * 获取 Long 类型数据
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    /**
     * 删除指定键的数据
     */
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    /**
     * 清空所有数据
     */
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
