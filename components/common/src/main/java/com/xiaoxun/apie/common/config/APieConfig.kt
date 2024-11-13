package com.xiaoxun.apie.common.config

import com.xiaoxun.apie.common.navbar.TabData

/**
 * 应用基础配置类
 */
object APieConfig {
    /**
     * tab的名称
     */
    fun getNavTabName(): MutableList<String> = mutableListOf("tab-1", "tab-2", "tab-3")

    /**
     * tab的lottie动画json
     */
    fun getNavTabData(): MutableList<TabData> {
        return mutableListOf<TabData>().apply {
            add(TabData(getNavTabName()[0], "home.json"))
            add(TabData(getNavTabName()[1], "category.json"))
            add(TabData(getNavTabName()[2], "mine.json"))
        }
    }
}