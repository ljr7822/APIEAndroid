package com.xiaoxun.apie.home_page.activity

import android.net.Uri
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.xiaoxun.apie.common.HOME_INDEX_ACTIVITY_PATH
import com.xiaoxun.apie.common.album.APieAlbumPickerHelper
import com.xiaoxun.apie.common.base.activity.APieBaseViewPagerActivity
import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.navbar.APieNavBarItem
import com.xiaoxun.apie.common.navbar.APieNavBarLayout
import com.xiaoxun.apie.common.upload.APieUploadHelper
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.alphaHide
import com.xiaoxun.apie.common.utils.alphaShow
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.gold_manage.service.GoldService
import com.xiaoxun.apie.home_page.adapter.APieViewPagerAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexActivityBinding
import com.xiaoxun.apie.home_page.fragment.plan.APieCreateFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexDesireFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexHomeFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexMineFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexStorageFragment
import com.xiaoxun.apie.home_page.fragment.desire.APieCreateDesireFragment
import com.xiaoxun.apie.home_page.repo.desire.IIndexDesireRepo
import com.xiaoxun.apie.home_page.repo.desire.IndexDesireRepoImpl
import com.xiaoxun.apie.home_page.repo.home.IIndexHomeRepo
import com.xiaoxun.apie.home_page.repo.home.IndexHomeRepoImpl
import com.xiaoxun.apie.home_page.viewmodel.GenericViewModelFactory
import com.xiaoxun.apie.home_page.viewmodel.IndexDesireViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = HOME_INDEX_ACTIVITY_PATH)
class APieIndexActivity :
    APieBaseViewPagerActivity<LayoutApieIndexActivityBinding, APieViewPagerAdapter>(
        LayoutApieIndexActivityBinding::inflate
    ) {

    private var selectedTabIndex = 0

    private var coroutineJob: Job? = null

    private val mediaPickerLauncher = APieAlbumPickerHelper.registerPicker(
        this,
        null,
        object : APieAlbumPickerHelper.MediaPickerCallback {
            override fun onMediaSelected(uris: List<Uri>) {
                // 处理选中的媒体
                APieLog.d("ljrxxx", "onMediaSelected: $uris")
                APieUploadHelper.uploadImage(this@APieIndexActivity, uris[0])
            }

            override fun onCanceled() {
                // 用户取消选择
            }
        })

    private val homeViewModel: IndexHomeViewModel by lazy {
        ViewModelProvider(
            this@APieIndexActivity,
            GenericViewModelFactory { IndexHomeViewModel() })[IndexHomeViewModel::class.java]
    }

    private val desireViewModel: IndexDesireViewModel by lazy {
        ViewModelProvider(
            this@APieIndexActivity,
            GenericViewModelFactory { IndexDesireViewModel() })[IndexDesireViewModel::class.java]
    }

    private val goldService: GoldService by lazy { GoldService() }

    private val planRepo: IIndexHomeRepo by lazy { IndexHomeRepoImpl(homeViewModel, goldService) }

    private val desireRepo: IIndexDesireRepo by lazy { IndexDesireRepoImpl(desireViewModel, goldService) }

    override fun createAdapter(): APieViewPagerAdapter {
        return APieViewPagerAdapter(this, mFragmentList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavItemData()
        initObserver()
    }

    override fun initializeView() {
        super.initializeView()
        binding.createBtn.setDebouncingClickListener {
            if (selectedTabIndex == APieConfig.HOME_PAGE_INDEX) {
                APieCreateFragment(planRepo, homeViewModel).show(supportFragmentManager, "create_plan")
            } else if (selectedTabIndex == APieConfig.DESIRE_PAGE_INDEX) {
                APieCreateDesireFragment(desireRepo, desireViewModel).show(supportFragmentManager, "create_desire")
            }
//            openPhotoPicker()
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

    private fun initObserver() {
        observe(homeViewModel.listScrolling) {
            startFloatBtnAnim(it)
        }
        observe(desireViewModel.listScrolling) {
            startFloatBtnAnim(it)
        }
    }

    private fun startFloatBtnAnim(isScrolling: Boolean) {
        // 滚动时向下隐藏，停止滚动时向上出现
        val translationX =
            if (isScrolling) (UIUtils.getScreenRealWidth(this) / 2f) + (binding.createBtn.height.toFloat() / 3f) else 0f
        val rotation = if (isScrolling) 360f else 0f
        val alpha = if (isScrolling) 0.9f else 1f // 透明度动画
        // 停止滚动时延迟 1.5 秒再显示按钮
        if (!isScrolling) {
            // 取消之前的任务（如果存在）
            coroutineJob?.cancel()
            // 启动新的协程任务
            coroutineJob = lifecycleScope.launch(Dispatchers.Main) {
                delay(1500L)
                binding.createBtn.animate()
                    .translationX(translationX)
                    .rotation(rotation)
                    .alpha(alpha)
                    .setDuration(400L)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
        } else {
            binding.createBtn.animate()
                .translationX(translationX)
                .rotation(rotation)
                .alpha(alpha)
                .setDuration(400L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }


    override fun setupViewPager() {
        super.setupViewPager()
        setViewPagerSlideEnabled(false)
    }

    private fun initNavItemData() {
        getNavTabNames().forEachIndexed { index, tabName ->
            when (index) {
                APieConfig.HOME_PAGE_INDEX -> {
                    val fragment = APieIndexHomeFragment().apply {
                        arguments = Bundle().apply {
                            putString(APieIndexHomeFragment.TEST_FLAG, tabName)
                        }
                    }
                    mFragmentList.add(fragment)
                }

                APieConfig.DESIRE_PAGE_INDEX -> {
                    val fragment = APieIndexDesireFragment().apply {
                        arguments = Bundle().apply {
                            putString(APieIndexDesireFragment.TEST_FLAG, tabName)
                        }
                    }
                    mFragmentList.add(fragment)
                }

                APieConfig.STORAGE_PAGE_INDEX -> {
                    val fragment = APieIndexStorageFragment().apply {
                        arguments = Bundle().apply {
                            putString(APieIndexDesireFragment.TEST_FLAG, tabName)
                        }
                    }
                    mFragmentList.add(fragment)
                }

                APieConfig.MINE_PAGE_INDEX -> {
                    val fragment = APieIndexMineFragment().apply {
                        arguments = Bundle().apply {
                            putString(APieIndexDesireFragment.TEST_FLAG, tabName)
                        }
                    }
                    mFragmentList.add(fragment)
                }
            }
        }
        mNavBarLayout?.apply {
            setData(getNavTabData())
            setViewPager(mVpContent)
            setSameTabClickCallBack(true)
            setOnItemSelectedListener(object : APieNavBarLayout.OnItemSelectedListener {
                override fun onItemSelected(
                    aPieNavBarItem: APieNavBarItem?,
                    previousPosition: Int,
                    currentPosition: Int
                ) {
                    APieLog.d(TAG, "currentPosition: $currentPosition, previousPosition: $previousPosition")
                    selectedTabIndex = currentPosition
                    if (currentPosition == APieConfig.MINE_PAGE_INDEX) {
                        binding.createBtn.alphaHide(200)
                    } else {
                        binding.createBtn.alphaShow(100)
                    }
                }

                override fun onItemSameTabClick(
                    aPieNavBarItem: APieNavBarItem?,
                    currentPosition: Int
                ) {
                    APieLog.d(TAG, "onItemSameTabClick: $currentPosition")
                    // TODO:连续点击同一个tab
                }
            })
        }
    }
}