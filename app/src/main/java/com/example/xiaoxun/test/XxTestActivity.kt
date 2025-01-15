package com.example.xiaoxun.test

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.xiaoxun.databinding.LayoutXxTestActivityBinding
import com.xiaoxun.apie.account_manager.repo.AccountDBRepository
import com.xiaoxun.apie.account_manager.repo.AccountDataDescriptor
import com.xiaoxun.apie.common.APP_XX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.ThreadUtil
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

@Route(path = APP_XX_ACTIVITY_PATH)
class XxTestActivity : APieBaseBindingActivity<LayoutXxTestActivityBinding>(
    LayoutXxTestActivityBinding::inflate
) {

    private val db: AccountDBRepository by lazy {
        AccountDBRepository(this)
    }
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

        binding.insert.setDebouncingClickListener {
            ThreadUtil.runOnChildThread {
                db.insertAccountData(
                    AccountDataDescriptor(
                        userId = "10827827189",
                        userName = "xiaoxun",
                        phoneNumber = "18289816889",
                        token = "shsoubzxuchiuhgiuqghwiu12vbds",
                        userPortrait = "www.baidu.com",
                        desc = "这是一个测试账号",
                        sex = 0,
                        address = "北京市",
                        grade = 999,
                        userType = 0
                    )
                )
            }
        }

        binding.find.setDebouncingClickListener {
            db.findAccountDataByUserId("10827827189")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding.data.text = it.toString()
                }, {
                    APieLog.e("ljrxxx",it.message.toString())
                })
        }

        binding.findTokenByUserId.setDebouncingClickListener {
            db.findAccountTokenByUserId("10827827189")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding.data.text = it
                }, {
                    APieLog.e("ljrxxx",it.message.toString())
                })
        }

        binding.findTokenByPhoneNum.setDebouncingClickListener {
            db.findAccountTokenByPhoneNum("18289816889")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding.data.text = it
                }, {
                    APieLog.e("ljrxxx",it.message.toString())
                })
        }
    }
}