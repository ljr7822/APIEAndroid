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
import com.xiaoxun.apie.common.intf.NavTabProvider
import com.xiaoxun.apie.common.navbar.APieNavBarLayout
import com.xiaoxun.apie.common.navbar.TabData

/**
 * viewPager + BottomBarLayout 实现的底部导航栏的基类
 */
abstract class APieBaseViewPagerActivity<VB : ViewBinding, FA : FragmentStateAdapter>(
    inflate: (LayoutInflater) -> VB,
    private val maskModel: Boolean = false
) : APieBaseBindingActivity<VB>(inflate), NavTabProvider {

    // 延迟初始化 ViewPager2 和 NavBar
    val mVpContent: ViewPager2 by lazy { binding.root.findViewById(R.id.vp_content) }
    val mNavBarLayout: APieNavBarLayout? by lazy { binding.root.findViewById(R.id.bbl) }

    // Fragment 列表
    val mFragmentList: MutableList<Fragment> = mutableListOf<Fragment>()

    // 抽象方法让子类提供适配器
    protected abstract fun createAdapter(): FA

    override fun getNavTabNames(): MutableList<String> =
        APieConfig.getNavTabMap().values.toMutableList()

    override fun getNavTabData(): MutableList<TabData> =
        APieConfig.getNavTabData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!maskModel) {
            initContentAndNavView()
            mVpContent.adapter = createAdapter()
        }
    }

    private fun initContentAndNavView() {
        setupViewPager()
        setupNavBar()
    }

    fun setViewPagerSlideEnabled(enabled: Boolean) {
        mVpContent.isUserInputEnabled = enabled
    }

    /**
     * 提供可重写的 ViewPager 初始化，已通过 lazy 初始化，无需额外代码
     */
    open fun setupViewPager() {}

    /**
     * 提供可重写的 NavBar 初始化，已通过 lazy 初始化，无需额外代码
     */
    open fun setupNavBar() {}
}
