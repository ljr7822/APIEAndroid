package com.example.xiaoxun.nav

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.navbar.APieNavBarLayout
import com.xiaoxun.apie.common.navbar.TabData
import com.xiaoxun.apie.common.navbar.fragment.TabFragment

/**
 * viewPager + BottomBarLayout 实现的底部导航栏的基类
 */
abstract class APieViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ViewBinding

    private lateinit var mVpContent: ViewPager2

    protected var mNavBarLayout: APieNavBarLayout? = null

    private val mFragmentList = mutableListOf<TabFragment>()

    protected abstract fun getViewBinding(): ViewBinding

    protected open fun getNavTabNames(): MutableList<String> = APieConfig.getNavTabName()

    protected open fun getNavTabData(): MutableList<TabData> = APieConfig.getNavTabData()

    open fun initView() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initWindowsStyle()
        initContentAndNavView()
        initNavItemData()
        initView()
    }

    private fun initWindowsStyle() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root.findViewById(com.example.xiaoxun.R.id.root_view)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    private fun initContentAndNavView() {
        mVpContent = binding.root.findViewById(R.id.vp_content)
        mNavBarLayout = binding.root.findViewById(R.id.bbl)
    }

    private fun initNavItemData() {
        getNavTabNames().forEach { tabContent ->
            val fragment = TabFragment().apply {
                arguments = Bundle().apply {
                    putString(TabFragment.CONTENT, tabContent)
                }
            }
            mFragmentList.add(fragment)
        }
        mNavBarLayout?.setData(getNavTabData())
        mVpContent.adapter = ViewPagerAdapter(this)
        mNavBarLayout?.setViewPager(mVpContent)
    }

    inner class ViewPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
        override fun getItemCount(): Int = mFragmentList.size

        override fun createFragment(position: Int): Fragment = mFragmentList[position]
    }
}
