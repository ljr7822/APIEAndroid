package com.xiaoxun.apie.common.base.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.config.APieConfig
import com.xiaoxun.apie.common.navbar.APieNavBarLayout
import com.xiaoxun.apie.common.navbar.TabData

/**
 * viewPager + BottomBarLayout 实现的底部导航栏的基类
 */
abstract class APieBaseViewPagerActivity<VB : ViewBinding>(
    private val inflate: (LayoutInflater) -> VB,
    private val maskModel: Boolean = false
) : AppCompatActivity() {

    lateinit var binding: VB

    lateinit var mVpContent: ViewPager2

    var mNavBarLayout: APieNavBarLayout? = null

    val mFragmentList: MutableList<Fragment> = mutableListOf<Fragment>()

    protected open fun getNavTabNames(): MutableList<String> =
        APieConfig.getNavTabMap().values.toMutableList()

    protected open fun getNavTabData(): MutableList<TabData> = APieConfig.getNavTabData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        if (!maskModel) {
            initContentAndNavView()
        }
    }

    private fun initContentAndNavView() {
        mVpContent = binding.root.findViewById(R.id.vp_content)
        mNavBarLayout = binding.root.findViewById(R.id.bbl)
    }

    inner class ViewPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
        override fun getItemCount(): Int = mFragmentList.size

        override fun createFragment(position: Int): Fragment = mFragmentList[position]
    }
}
