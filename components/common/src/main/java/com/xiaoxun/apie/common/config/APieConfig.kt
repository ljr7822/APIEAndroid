package com.xiaoxun.apie.common.config

import com.xiaoxun.apie.common.navbar.TabData
import com.xiaoxun.apie.common.utils.CurrencyType

/**
 * 应用基础配置类
 */
object APieConfig {
    const val HOME_PAGE_INDEX = 0
    const val DESIRE_PAGE_INDEX = 1
    const val STORAGE_PAGE_INDEX = 2
    const val MINE_PAGE_INDEX = 3

    private const val HOME_TAB_NAME = "计划"
    private const val DESIRE_TAB_NAME = "心愿"
    private const val STORAGE_TAB_NAME = "收纳"
    private const val MINE_TAB_NAME = "我的"

    private const val HOME_TAB_LOTTIE_JSON = "home.json"
    private const val DESIRE_TAB_LOTTIE_JSON = "category.json"
    private const val STORAGE_TAB_LOTTIE_JSON = "cart.json"
    private const val MINE_TAB_LOTTIE_JSON = "mine.json"

    const val PHONE_NUMBER_MAX_LENGTH = 11

    const val THING_APPENDIX_MAX_COUNT = 5

    private var DEF_CURRENCY_TYPE = CurrencyType.RMB

    const val DEFAULT_SMS_CODE_SIZE = 4

    const val DEFAULT_PASSWORD_SIZE = 6

    const val API_URL: String = "http://192.168.0.67:8090"

    /**
     * tab的名称
     */
    fun getNavTabMap(): MutableMap<Int, String> = mutableMapOf(
        Pair(HOME_PAGE_INDEX, HOME_TAB_NAME),
        Pair(DESIRE_PAGE_INDEX, DESIRE_TAB_NAME),
        Pair(STORAGE_PAGE_INDEX, STORAGE_TAB_NAME),
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
                    title = getNavTabMap().getValue(STORAGE_PAGE_INDEX),
                    lottieJson = STORAGE_TAB_LOTTIE_JSON
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

    fun setCurrencyType(currencyType: CurrencyType) {
        DEF_CURRENCY_TYPE = currencyType
    }

    fun getCurrencyType(): CurrencyType {
        return DEF_CURRENCY_TYPE
    }
}