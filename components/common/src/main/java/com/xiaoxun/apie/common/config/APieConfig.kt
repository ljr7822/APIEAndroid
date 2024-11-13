package com.xiaoxun.apie.common.config

import com.xiaoxun.apie.common.navbar.TabData

/**
 * 应用基础配置类
 */
object APieConfig {
    const val HOME_PAGE_INDEX = 0
    const val DESIRE_PAGE_INDEX = 1
    const val MINE_PAGE_INDEX = 2

    /**
     * tab的名称
     */
    fun getNavTabName(): MutableMap<Int,String> = mutableMapOf(
        Pair(HOME_PAGE_INDEX,"tab-1"),
        Pair(DESIRE_PAGE_INDEX,"tab-2"),
        Pair(MINE_PAGE_INDEX,"tab-3")
    )

    /**
     * tab的lottie动画json
     */
    fun getNavTabData(): MutableList<TabData> {
        return mutableListOf<TabData>().apply {
            add(TabData(getNavTabName().getValue(HOME_PAGE_INDEX), "home.json"))
            add(TabData(getNavTabName().getValue(DESIRE_PAGE_INDEX), "category.json"))
            add(TabData(getNavTabName().getValue(MINE_PAGE_INDEX), "mine.json"))
        }
    }
}