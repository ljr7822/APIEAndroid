package com.example.xiaoxun

import android.os.Bundle
import com.example.xiaoxun.databinding.LayoutApieHomeActivityBinding
import com.example.xiaoxun.fragment.DesireFragment
import com.example.xiaoxun.fragment.HomeFragment
import com.example.xiaoxun.fragment.MineFragment
import com.gyf.immersionbar.ImmersionBar
import com.xiaoxun.apie.common.base.activity.APieBaseViewPagerActivity

/**
 * 首页Activity
 */
class APieHomeActivity : APieBaseViewPagerActivity<LayoutApieHomeActivityBinding>(LayoutApieHomeActivityBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowsStyle()
        initNavItemData()
    }

    private fun initWindowsStyle() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(true)
            .init()
    }

    private fun initNavItemData() {
        getNavTabNames().forEachIndexed { index, tabName ->
            when (index) {
                0 -> {
                    val fragment = HomeFragment().apply {
                        arguments = Bundle().apply {
                            putString(HomeFragment.TEST_FLAG, tabName)
                        }
                    }
                    mFragmentList.add(fragment)
                }
                1 -> {
                    val fragment = DesireFragment().apply {
                        arguments = Bundle().apply {
                            putString(DesireFragment.TEST_FLAG, tabName)
                        }
                    }
                    mFragmentList.add(fragment)
                }
                2 -> {
                    val fragment = MineFragment().apply {
                        arguments = Bundle().apply {
                            putString(DesireFragment.TEST_FLAG, tabName)
                        }
                    }
                    mFragmentList.add(fragment)
                }
            }
        }
        mNavBarLayout?.setData(getNavTabData())
        mVpContent.adapter = ViewPagerAdapter(this)
        mNavBarLayout?.setViewPager(mVpContent)
    }
}