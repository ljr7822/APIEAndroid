package com.example.xiaoxun.test

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.xiaoxun.databinding.LayoutXxTestActivityBinding
import com.xiaoxun.apie.account_manager.repo.AccountDBRepository
import com.xiaoxun.apie.account_manager.repo.AccountDataDescriptor
import com.xiaoxun.apie.common.APP_XX_ACTIVITY_PATH
import com.xiaoxun.apie.common.album.APieAlbumPickerHelper
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadImage
import com.xiaoxun.apie.common.ui.easy_glide.progress.OnProgressListener
import com.xiaoxun.apie.common.utils.APieImageDownloadUtils
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.ThreadUtil
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
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

    private val mediaPickerLauncher = APieAlbumPickerHelper.registerPicker(
        this,
        null,
        object : APieAlbumPickerHelper.MediaPickerCallback {
            override fun onMediaSelected(uris: List<Uri>) {
                // 处理选中的媒体
                APieLog.d("ljrxxx", "onMediaSelected: $uris")
            }

            override fun onCanceled() {
                // 用户取消选择
            }
        })

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
                    APieLog.e("ljrxxx", it.message.toString())
                })
        }

        binding.findTokenByUserId.setDebouncingClickListener {
            db.findAccountTokenByUserId("10827827189")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding.data.text = it
                }, {
                    APieLog.e("ljrxxx", it.message.toString())
                })
        }

        binding.findTokenByPhoneNum.setDebouncingClickListener {
            db.findAccountTokenByPhoneNum("18289816889")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    binding.data.text = it
                }, {
                    APieLog.e("ljrxxx", it.message.toString())
                })
        }

        binding.loadImage.loadImage(
            context = this,
            url = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
            onProgressListener = object : OnProgressListener {
                override fun onProgress(
                    isComplete: Boolean,
                    percentage: Int,
                    bytesRead: Long,
                    totalBytes: Long
                ) {
                    APieLog.d(
                        "ljrxxx",
                        "onProgress: $percentage, totalBytes: $totalBytes, bytesRead: $bytesRead"
                    )
                }
            })

        binding.downloadImageToGallery.setDebouncingClickListener {
            APieImageDownloadUtils.downloadImageToGallery(
                this,
                "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg"
            )
        }

        binding.pickMediaButton.setDebouncingClickListener {
            openPhotoPicker()
        }

        binding.requestPermission.setDebouncingClickListener {

        }
    }

    /**
     * 打开图片选择器
     */
    private fun openPhotoPicker() {
        mediaPickerLauncher?.let {
            APieAlbumPickerHelper.showPickerDialog(
                fragmentManager = supportFragmentManager,
                launcher = it,
                mediaType = "image/*",
                allowMultiple = false
            )
        }
    }
}