package com.example.xiaoxun.test

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.xiaoxun.apie.common.utils.dp

class ViewPagerAdapter(
    private val categoryList: List<CategoryBean>
) : RecyclerView.Adapter<ViewPagerAdapter.PageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val marginHorizontal = 10.dp
        val recyclerView = RecyclerView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).apply {
                marginStart = marginHorizontal
                marginEnd = marginHorizontal
            }
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        }
        return PageViewHolder(recyclerView)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val templates = categoryList[position].templates
        val adapter = TemplateAdapter()
        holder.recyclerView.adapter = adapter
        adapter.submitList(templates) // 加载分类下的模板
    }

    override fun getItemCount(): Int = categoryList.size

    class PageViewHolder(val recyclerView: RecyclerView) : RecyclerView.ViewHolder(recyclerView)
}
