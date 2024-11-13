package com.example.xiaoxun

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.xiaoxun.databinding.LayoutApieHomeActivityBinding
import com.example.xiaoxun.fragment.HomeFragment
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
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root.findViewById(R.id.root_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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