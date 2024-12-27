package com.xiaoxun.apie.home_page.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class APieViewPagerAdapter(
    fm: FragmentActivity,
    private val fragmentList: List<Fragment>
) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]
}