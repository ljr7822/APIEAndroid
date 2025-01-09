package com.xiaoxun.apie.common.utils.sound_pool

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.xiaoxun.apie.common.R

/**
 * 内置声音播放类
 */
class APieSoundPoolHelper private constructor(context: Context) {
    private val soundPool: SoundPool
    private val soundMap = mutableMapOf<BuiltInSound, Int>() // 使用枚举管理内置音效
    private var isLoaded = false // 标志是否所有音效加载完成

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(MAX_STREAMS) // 最大同时播放音效数
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                isLoaded = true
            }
        }

        // 加载内置音效资源
        loadBuiltInSounds(context)
    }

    /**
     * 加载内置的音效资源
     *
     * @param context 上下文
     */
    private fun loadBuiltInSounds(context: Context) {
        soundMap[BuiltInSound.NEW_MESSAGE] = soundPool.load(context, R.raw.newmsg, 1)
        soundMap[BuiltInSound.SHAKE] = soundPool.load(context, R.raw.audio_shake, 1)
        soundMap[BuiltInSound.END_TIP] = soundPool.load(context, R.raw.audio_end_tip, 1)
        soundMap[BuiltInSound.CATCH_ON] = soundPool.load(context, R.raw.catch_on, 1)
    }

    /**
     * 播放指定的内置音效
     *
     * @param sound 内置音效枚举值
     * @param loop 是否循环播放 (0 表示不循环，-1 表示无限循环)
     */
    fun playBuiltIn(sound: BuiltInSound, loop: Int = 0) {
        if (!isLoaded) {
            throw IllegalStateException("SoundPool 未加载完成，请稍后再试！")
        }
        val soundId =
            soundMap[sound] ?: throw IllegalArgumentException("音效 ${sound.name} 未找到！")
        soundPool.play(soundId, 1f, 1f, 1, loop, 1f)
    }

    /**
     * 停止播放指定的内置音效
     *
     * @param sound 内置音效枚举值
     */
    fun stopBuiltIn(sound: BuiltInSound) {
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

    companion object {
        private const val MAX_STREAMS = 10 // 最大同时播放音效数量

        @Volatile
        private var instance: APieSoundPoolHelper? = null

        /**
         * 获取单例实例
         *
         * @param context 上下文
         */
        fun getInstance(context: Context): APieSoundPoolHelper {
            return instance ?: synchronized(this) {
                instance ?: APieSoundPoolHelper(context.applicationContext).also { instance = it }
            }
        }
    }
}