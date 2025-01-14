package com.example.xiaoxun.test

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.xiaoxun.databinding.LayoutXxTestActivityBinding
import com.xiaoxun.apie.common.APP_XX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.sound_pool.APieSoundPoolHelper
import com.xiaoxun.apie.common.utils.sound_pool.SoundInfo
import kotlinx.coroutines.launch

@Route(path = APP_XX_ACTIVITY_PATH)
class XxTestActivity : APieBaseBindingActivity<LayoutXxTestActivityBinding>(
    LayoutXxTestActivityBinding::inflate
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.saveData.setDebouncingClickListener {
            lifecycleScope.launch {
                TestRepository.userName.set { "xiaoxun" }
                TestRepository.userId.set { "123456" }
                TestRepository.token.set { "xaoaoaisusdns" }
                TestRepository.phoneNum.set { "18289816889" }

            }
        }

        binding.getData.setDebouncingClickListener {
            lifecycleScope.launch {
                binding.data.text = TestRepository.token.get()
            }
        }

        TestRepository.userId.asLiveData()
            .observe(this) {
                binding.data.text = it ?: ""
            }
    }
}