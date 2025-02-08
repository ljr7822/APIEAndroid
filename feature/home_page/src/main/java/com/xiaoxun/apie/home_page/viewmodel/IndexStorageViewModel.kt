package com.xiaoxun.apie.home_page.viewmodel

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.base.viewmodel.APieBaseViewModel

class IndexStorageViewModel: APieBaseViewModel() {

    // ********************************************* 状态 *********************************************


    // ********************************************* 数据 *********************************************
    // 事物图标URL
    private val _thingIconUrl: MutableLiveData<String> = MutableLiveData()
    val thingIconUrl: MutableLiveData<String> get() = _thingIconUrl

    // ********************************************* 方法 *********************************************
    fun updateThingIconUrl(url: String) {
        _thingIconUrl.value = url
    }
}