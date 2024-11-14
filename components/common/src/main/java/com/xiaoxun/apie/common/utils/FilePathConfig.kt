package com.xiaoxun.apie.common.utils

import com.xiaoxun.apie.common.base.APieUtilsCenter

object FilePathConfig {

    // 内置私有目录：data/data/package/files/xiaoxun/
    val APIE_PRIVATE_DIR = APieUtilsCenter.getApp()?.filesDir?.absolutePath + "/xiaoxun"
    // 接口缓存路径 data/data/package/files/xiaoxun/data_cache
    val DATA_CACHE_DIR = "$APIE_PRIVATE_DIR/data_cache"
    // 素材缓存路径 data/data/package/files/xiaoxun/material
    val MATERIAL_DIR = "$APIE_PRIVATE_DIR/material"
}