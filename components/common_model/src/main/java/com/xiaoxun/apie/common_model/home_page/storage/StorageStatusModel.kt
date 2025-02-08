package com.xiaoxun.apie.common_model.home_page.storage

data class StorageStatusModel(
    val statusType: StatusType
)

enum class StatusType(val type: Int, val statusName: String) {
    USEING(1, "使用中"),    // 使用中
    RETIRED(2, "已退役"),   // 已退役
    DAMAGED(3, "已损坏"),   // 已损坏
    LOST(4, "已丢失"),      // 已丢失
    DUSTY(5, "吃灰中"),     // 吃灰中
    SOLD(6, "已售出");      // 已售出

    companion object {
        // 根据type值获取对应的枚举
        fun fromType(type: Int): StatusType? {
            return entries.find { it.type == type }
        }
    }
}

