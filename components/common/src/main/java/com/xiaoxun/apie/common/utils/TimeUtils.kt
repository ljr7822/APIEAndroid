package com.xiaoxun.apie.common.utils

/**
 * 将时间戳集合格式化为指定日期格式的集合
 * @return MutableList<String> [2021-09-09,2021-09-10,...]
 */
fun MutableList<Long>.toFormatList(format: String = "yyyy-MM-dd"): MutableList<String> {
    val formatList = mutableListOf<String>()
    for (i in this) {
        formatList.add(DateTimeUtils.conversionTime(i, format))
    }
    return formatList
}