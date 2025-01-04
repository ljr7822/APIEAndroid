package com.xiaoxun.apie.home_page.activity

import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.xiaoxun.apie.common.HOME_INDEX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseViewPagerActivity
import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.adapter.APieViewPagerAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexActivityBinding
import com.xiaoxun.apie.home_page.fragment.APieCreateFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexDesireFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexHomeFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexMineFragment
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.repo.IndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Route(path = HOME_INDEX_ACTIVITY_PATH)
class APieIndexActivity :
    APieBaseViewPagerActivity<LayoutApieIndexActivityBinding, APieViewPagerAdapter>(
        LayoutApieIndexActivityBinding::inflate
    ) {

    private var coroutineJob: Job? = null

    private val viewModel: IndexHomeViewModel by lazy {
        ViewModelProvider(this@APieIndexActivity, IndexHomeViewModelFactory()).get(
            IndexHomeViewModel::class.java
        )
    }

    private val repo: IIndexHomeRepo by lazy { IndexHomeRepo(viewModel) }

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
//            ARouter.getInstance().build(HOME_CREATE_PLAN_ACTIVITY_PATH).navigation()
            APieCreateFragment(repo, viewModel).show(supportFragmentManager, "create_plan")
        }
    }

    private fun initObserver() {
        observe(viewModel.listScrolling) {
            startFloatBtnAnim(it)
        }
    }

    private fun startFloatBtnAnim(isScrolling: Boolean) {
        // 滚动时向下隐藏，停止滚动时向上出现
        val translationY = if (isScrolling) binding.createBtn.height.toFloat() + 250f else 0f
        val rotation = if (isScrolling) 360f else 0f
        val alpha = if (isScrolling) 0f else 1f // 透明度动画
        // 停止滚动时延迟 1 秒再显示按钮
        if (!isScrolling) {
            // 取消之前的任务（如果存在）
            coroutineJob?.cancel()
            // 启动新的协程任务
            coroutineJob = GlobalScope.launch(Dispatchers.Main) {
                delay(1000L)
                binding.createBtn.animate()
                    .translationY(translationY)
                    .rotation(rotation)
                    .alpha(alpha)
                    .setDuration(400L)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
        } else {
            binding.createBtn.animate()
                .translationY(translationY)
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
        mNavBarLayout?.setData(getNavTabData())
        mNavBarLayout?.setViewPager(mVpContent)
    }
}