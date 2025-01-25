package com.example.xiaoxun.test

import android.graphics.drawable.DrawableWrapper
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.xiaoxun.databinding.LayoutXxViewpagerActivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xiaoxun.apie.common.APP_XX_VIEWPAGER_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity

@Route(path = APP_XX_VIEWPAGER_ACTIVITY_PATH)
class XxViewPagerActivity :
    APieBaseBindingActivity<LayoutXxViewpagerActivityBinding>(LayoutXxViewpagerActivityBinding::inflate) {

    private lateinit var adapter: ViewPagerAdapter
    private var categoryList: List<CategoryBean> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }

    private fun loadData() {
        // 模拟接口返回数据
        val data = TemplateCategoryList(
            categoryList = mutableListOf(
                CategoryBean("推荐", mutableListOf(Template("Template 1", "Content 1"),
                    Template("Template 2", "Content 2"),
                    Template("Template 3", "Content 3"),
                    Template("Template 4", "Content 4"),
                    Template("Template 5", "Content 5"),
                    Template("Template 6", "Content 6"),
                    Template("Template 1", "Content 1"),
                    Template("Template 2", "Content 2"),
                    Template("Template 3", "Content 3"),
                    Template("Template 4", "Content 4"),
                    Template("Template 5", "Content 5"),
                    Template("Template 6", "Content 6"))
                ),
                CategoryBean("美食", mutableListOf(Template("Template 1", "Content 1"))),
                CategoryBean("旅游", mutableListOf(Template("Template 1", "Content 1"))),
                CategoryBean("宝妈日常", mutableListOf(Template("Template 1", "Content 1"))),
                CategoryBean("攻略", mutableListOf(Template("Template 1", "Content 1"))),
                CategoryBean("测试", mutableListOf()),
                CategoryBean("测试", mutableListOf()),
                CategoryBean("测试", mutableListOf()),
                CategoryBean("测试", mutableListOf())
            )
        )
        categoryList = data.categoryList

        setupViewPagerAndTabs()
    }

    private fun setupViewPagerAndTabs() {
        // 初始化 ViewPager2 的适配器
        adapter = ViewPagerAdapter(categoryList)
        binding.viewPager.adapter = adapter

        binding.tabLayout.setSelectedTabIndicatorFixWidth(100)

        // 绑定 TabLayout 和 ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categoryList[position].categoryName
        }.attach()


    }
}

fun TabLayout.setSelectedTabIndicatorFixWidth(width: Int) {
    // TabLayout未提供固定宽度方法，而且无法插手计算宽度逻辑，还好最后是用drawable.setBounds()绘制的宽度。
    this.setSelectedTabIndicator(object : DrawableWrapper(this.tabSelectedIndicator) {
        override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
            var realLeft = left
            var realRight = right
            if (right - left != width) {
                val center = left + (right - left) / 2
                realLeft = center - width / 2
                realRight = center + width / 2
            }
            super.setBounds(realLeft, top, realRight, bottom)
        }
    })
}


data class TemplateCategoryList(
    val categoryList: MutableList<CategoryBean>
)

data class CategoryBean(
    val categoryName: String,
    val templates: MutableList<Template>
)

data class Template(
    val name: String,
    val content: String
)