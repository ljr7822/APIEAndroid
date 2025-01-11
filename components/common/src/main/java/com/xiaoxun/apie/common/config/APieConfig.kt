package com.xiaoxun.apie.common.config

import com.xiaoxun.apie.common.navbar.TabData
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper

/**
 * 应用基础配置类
 */
object APieConfig {
    const val HOME_PAGE_INDEX = 0
    const val DESIRE_PAGE_INDEX = 1
    const val MINE_PAGE_INDEX = 2

    private const val HOME_TAB_NAME = "计划"
    private const val DESIRE_TAB_NAME = "心愿"
    private const val MINE_TAB_NAME = "我的"

    private const val HOME_TAB_LOTTIE_JSON = "home.json"
    private const val DESIRE_TAB_LOTTIE_JSON = "category.json"
    private const val MINE_TAB_LOTTIE_JSON = "mine.json"

    const val PHONE_NUMBER_MAX_LENGTH = 11

    const val API_URL: String = "http://192.168.0.33:8090"

    /**
     * tab的名称
     */
    fun getNavTabMap(): MutableMap<Int, String> = mutableMapOf(
        Pair(HOME_PAGE_INDEX, HOME_TAB_NAME),
        Pair(DESIRE_PAGE_INDEX, DESIRE_TAB_NAME),
        Pair(MINE_PAGE_INDEX, MINE_TAB_NAME)
    )

    /**
     * tab的lottie动画json
     */
    fun getNavTabData(): MutableList<TabData> {
        return mutableListOf<TabData>().apply {
            add(
                TabData(
                    title = getNavTabMap().getValue(HOME_PAGE_INDEX),
                    lottieJson = HOME_TAB_LOTTIE_JSON
                )
            )
            add(
                TabData(
                    title = getNavTabMap().getValue(DESIRE_PAGE_INDEX),
                    lottieJson = DESIRE_TAB_LOTTIE_JSON
                )
            )
            add(
                TabData(
                    title = getNavTabMap().getValue(MINE_PAGE_INDEX),
                    lottieJson = MINE_TAB_LOTTIE_JSON
                )
            )
        }
    }
}