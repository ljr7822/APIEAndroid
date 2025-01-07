package com.xiaoxun.apie.common.utils

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import com.xiaoxun.apie.common.base.APieUtilsCenter

/**
 * 震动帮助类.
 */
object APieVibrateTool {
    private var vibrator: Vibrator? = null
    private const val VIBRATION_DURATION: Long = 10 //millisecond


    fun device(time: Long = VIBRATION_DURATION) {
        if (vibrator == null) {
            vibrator =
                ContextCompat.getSystemService(APieUtilsCenter.getApp(), Vibrator::class.java)
        }
        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.EFFECT_TICK))
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(time)
            }
        }
    }
}