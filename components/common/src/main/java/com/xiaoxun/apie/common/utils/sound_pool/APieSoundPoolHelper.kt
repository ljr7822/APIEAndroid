package com.xiaoxun.apie.common.utils.sound_pool

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * 内置声音播放类
 */
private const val MAX_STREAMS = 10

object APieSoundPoolHelper {
    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<SoundInfo, Int>()
    private var isLoaded = false

    /**
     * 初始化SoundPool和内置音效
     */
    fun init(context: Context) {
        if (::soundPool.isInitialized) {
            return // 已经初始化过，不再重复初始化
        }

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(MAX_STREAMS)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                isLoaded = true
            }
        }

        loadBuiltInSounds(context)
    }

    /**
     * 加载内置的音效资源
     */
    private fun loadBuiltInSounds(context: Context) {
        soundMap[SoundInfo.NEW_MESSAGE] = soundPool.load(context, SoundInfo.NEW_MESSAGE.soundResId, 1)
        soundMap[SoundInfo.SHAKE] = soundPool.load(context, SoundInfo.SHAKE.soundResId, 1)
        soundMap[SoundInfo.END_TIP] = soundPool.load(context,SoundInfo.END_TIP.soundResId, 1)
        soundMap[SoundInfo.CATCH_ON] = soundPool.load(context, SoundInfo.CATCH_ON.soundResId, 1)
        soundMap[SoundInfo.SLOWER] = soundPool.load(context, SoundInfo.SLOWER.soundResId, 1)
        soundMap[SoundInfo.WATER_DROPLETS] = soundPool.load(context, SoundInfo.WATER_DROPLETS.soundResId, 1)
    }

    /**
     * 播放指定的内置音效
     */
    fun playBuiltIn(sound: SoundInfo, loop: Int = 0) {
        if (!isLoaded) {
            throw IllegalStateException("SoundPool 未加载完成，请稍后再试！")
        }
        val soundId =
            soundMap[sound] ?: throw IllegalArgumentException("音效 ${sound.name} 未找到！")
        soundPool.play(soundId, 1f, 1f, 1, loop, 1f)
    }

    /**
     * 获取指定的内置音效信息
     */
    fun playSoundInfoById(soundId: Int, loop: Int = 0) {
        if (!isLoaded) {
            throw IllegalStateException("SoundPool 未加载完成，请稍后再试！")
        }
        val soundInf = getSoundInfById(soundId)
        soundMap[soundInf]?.let {
            soundPool.play(it, 1f, 1f, 1, loop, 1f)
        }
    }

    /**
     * 停止播放指定的内置音效
     */
    fun stopBuiltIn(sound: SoundInfo) {
        val soundId = soundMap[sound] ?: return
        soundPool.stop(soundId)
    }

    /**
     * 释放资源
     */
    fun release() {
        soundPool.release()
        soundMap.clear()
    }
}