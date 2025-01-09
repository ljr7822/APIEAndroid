package com.example.xiaoxun

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.xiaoxun.databinding.LayoutXxTestActivityBinding
import com.xiaoxun.apie.common.APP_XX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.sound_pool.APieSoundPoolHelper
import com.xiaoxun.apie.common.utils.sound_pool.BuiltInSound

@Route(path = APP_XX_ACTIVITY_PATH)
class XxTestActivity : APieBaseBindingActivity<LayoutXxTestActivityBinding>(
    LayoutXxTestActivityBinding::inflate
) {

    private lateinit var soundPoolHelper: APieSoundPoolHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundPoolHelper = APieSoundPoolHelper.getInstance(this)

        binding.newMessage.setDebouncingClickListener {
            soundPoolHelper.playBuiltIn(BuiltInSound.NEW_MESSAGE)
        }

        binding.shake.setDebouncingClickListener {
            soundPoolHelper.playBuiltIn(BuiltInSound.SHAKE)
        }

        binding.endTip.setDebouncingClickListener {
            soundPoolHelper.playBuiltIn(BuiltInSound.END_TIP)
        }

        binding.catchOn.setDebouncingClickListener {
            soundPoolHelper.playBuiltIn(BuiltInSound.CATCH_ON)
        }
    }
}