package com.xiaoxun.apie.common.utils.sound_pool

import com.xiaoxun.apie.common.R

enum class SoundInfo(val soundId: Int, val soundResId: Int, val soundName: String) {
    NEW_MESSAGE(1, R.raw.newmsg, "音符悦动"),
    SHAKE(2, R.raw.audio_shake, "钟声回响"),
    END_TIP(3, R.raw.audio_end_tip, "节日怪诞"),
    CATCH_ON(4, R.raw.catch_on, "截取"),
    SLOWER(5, R.raw.slower, "反弹"),
    WATER_DROPLETS(6, R.raw.water_droplets, "水滴"),
}

fun getSoundInfById(soundId: Int): SoundInfo {
    return SoundInfo.entries.first { it.soundId == soundId }
}