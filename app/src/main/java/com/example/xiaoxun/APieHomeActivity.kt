package com.example.xiaoxun

import android.os.Bundle
import com.example.xiaoxun.databinding.LayoutApieHomeActivityBinding
import com.example.xiaoxun.fragment.HomeFragment
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
        getNavTabNames().forEach { tabContent ->
            val fragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(HomeFragment.TEST_FLAG, tabContent)
                }
            }
            mFragmentList.add(fragment)
        }
        mNavBarLayout?.setData(getNavTabData())
        mVpContent.adapter = ViewPagerAdapter(this)
        mNavBarLayout?.setViewPager(mVpContent)
    }
}