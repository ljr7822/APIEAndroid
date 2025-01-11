package com.example.xiaoxun

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.xiaoxun.databinding.LayoutXxTestActivityBinding
import com.xiaoxun.apie.common.APP_XX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.sound_pool.APieSoundPoolHelper
import com.xiaoxun.apie.common.utils.sound_pool.SoundInfo

@Route(path = APP_XX_ACTIVITY_PATH)
class XxTestActivity : APieBaseBindingActivity<LayoutXxTestActivityBinding>(
    LayoutXxTestActivityBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.newMessage.setDebouncingClickListener {
            APieSoundPoolHelper.playBuiltIn(SoundInfo.NEW_MESSAGE)
        }

        binding.shake.setDebouncingClickListener {
            APieSoundPoolHelper.playBuiltIn(SoundInfo.SHAKE)
        }

        binding.endTip.setDebouncingClickListener {
            APieSoundPoolHelper.playBuiltIn(SoundInfo.END_TIP)
        }

        binding.catchOn.setDebouncingClickListener {
            APieSoundPoolHelper.playBuiltIn(SoundInfo.CATCH_ON)
        }
    }
}