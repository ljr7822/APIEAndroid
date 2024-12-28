package com.xiaoxun.apie.home_page.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.xiaoxun.apie.common.HOME_CREATE_PLAN_ACTIVITY_PATH
import com.xiaoxun.apie.common.HOME_INDEX_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseViewPagerActivity
import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.adapter.APieViewPagerAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexActivityBinding
import com.xiaoxun.apie.home_page.fragment.APieIndexDesireFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexHomeFragment
import com.xiaoxun.apie.home_page.fragment.APieIndexMineFragment

@Route(path = HOME_INDEX_ACTIVITY_PATH)
class APieIndexActivity :
    APieBaseViewPagerActivity<LayoutApieIndexActivityBinding, APieViewPagerAdapter>(
        LayoutApieIndexActivityBinding::inflate
    ) {

    override fun createAdapter(): APieViewPagerAdapter {
        return APieViewPagerAdapter(this, mFragmentList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavItemData()
    }

    override fun initializeView() {
        super.initializeView()
        binding.createBtn.setDebouncingClickListener {
            ARouter.getInstance().build(HOME_CREATE_PLAN_ACTIVITY_PATH).navigation()
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