package com.xiaoxun.apie.common.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    const val EMPTY_STRING: String = ""
    const val FORMAT_TYPE_YMD = "yyyy-MM-dd"
    const val FORMAT_TYPE_YMD_STYLE = "yyyy/MM/dd"
    const val FORMAT_TYPE_YMDHMS = "yyyy-MM-dd HH:mm:ss"

    fun conversionTime(time: String, format: String = FORMAT_TYPE_YMD): Long? {
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val ofPattern = DateTimeFormatter.ofPattern(format)
                LocalDateTime.parse(time, ofPattern).toInstant(ZoneOffset.ofHours(8)).toEpochMilli()
            } else {
                val sdf = SimpleDateFormat(format, Locale.getDefault())
                sdf.parse(time)?.time
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun conversionTime(time: Long, format: String = FORMAT_TYPE_YMD): String {
        return DateFormat.format(format, time).toString()
    }

    fun formatDate(dataTime: Date = Date(), format: String = FORMAT_TYPE_YMD_STYLE): String? {
        return SimpleDateFormat(format, Locale.getDefault()).format(dataTime)
    }

    fun getWeekName(time: Long): String {
        val c = Calendar.getInstance()
        c.timeInMillis = time
        return when (c[Calendar.DAY_OF_WEEK]) {
            Calendar.SUNDAY -> "周日"
            Calendar.MONDAY -> "周一"
            Calendar.TUESDAY -> "周二"
            Calendar.WEDNESDAY -> "周三"
            Calendar.THURSDAY -> "周四"
            Calendar.FRIDAY -> "周五"
            Calendar.SATURDAY -> "周六"
            else -> EMPTY_STRING
        }
    }

    /**
     * 计算两个时间戳之间的时间差
     * @param startTime 开始时间戳（毫秒）
     * @param stopTime 结束时间戳（毫秒）
     * @param unit 时间单位（DAY、WEEK、MONTH、YEAR）
     */
    fun calculateTimeDifference(startTime: Long, stopTime: Long, unit: UnitType): Int {
        if (startTime <= 0 || stopTime <= 0) return 0
        val diffMillis = stopTime - startTime
        return when (unit) {
            UnitType.DAY -> (diffMillis / (1000 * 60 * 60 * 24)).toInt()
            UnitType.WEEK -> (diffMillis / (1000 * 60 * 60 * 24 * 7)).toInt()
            UnitType.MONTH -> (diffMillis / (1000 * 60 * 60 * 24 * 30)).toInt()
            UnitType.YEAR -> (diffMillis / (1000 * 60 * 60 * 24 * 365)).toInt()
        }
    }

    fun timestampToDate(timeStamp: Long): Date {
        return Date(timeStamp)
    }
}

/**
 * 时间单位
 */
enum class UnitType {
    DAY, WEEK, MONTH, YEAR
}