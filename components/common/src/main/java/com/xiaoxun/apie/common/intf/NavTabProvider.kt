package com.xiaoxun.apie.common.intf

import com.xiaoxun.apie.common.navbar.TabData

interface NavTabProvider {
    fun getNavTabNames(): MutableList<String>
    fun getNavTabData(): MutableList<TabData>
}