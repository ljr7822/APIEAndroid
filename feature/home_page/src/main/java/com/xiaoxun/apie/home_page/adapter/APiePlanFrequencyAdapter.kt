package com.xiaoxun.apie.home_page.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.R
import com.xiaoxun.apie.home_page.viewmodel.PlanListType

class APiePlanFrequencyAdapter(
    private val items: MutableList<PlanListType> = mutableListOf(),
    val itemClick: (Int, PlanListType) -> Unit = { _, _ -> } // 返回选中项的 position 和对应的数据项
) : RecyclerView.Adapter<APiePlanFrequencyAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1 // 记录当前选中的位置

    @MainThread
    fun replayData(newData: List<PlanListType>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planFrequencyName: TextView = view.findViewById(R.id.planFrequencyName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_plan_frequency_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item = items[position]
        holder.planFrequencyName.text = item.desc
        if (position == selectedPosition) {
            holder.planFrequencyName.setBackgroundResource(R.drawable.apie_frequency_plan_selected_bg)
            holder.planFrequencyName.setTextColor(holder.itemView.context.getColor(com.xiaoxun.apie.common.R.color.apie_color_6F94F4))
            holder.planFrequencyName.setTypeface(null, Typeface.BOLD)
        } else {
            holder.planFrequencyName.setBackgroundResource(R.drawable.apie_frequenct_plan_normal_bg)
            holder.planFrequencyName.setTextColor(holder.itemView.context.getColor(com.xiaoxun.apie.common.R.color.apieTheme_colorBlack_alpha_40))
            holder.planFrequencyName.setTypeface(null, Typeface.NORMAL) // 正常字体
        }
        holder.planFrequencyName.setDebouncingClickListener {
            updateSelectedPosition(position)
            itemClick(position, item)
        }
    }

    fun updateSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition) // 更新之前选中的项
        notifyItemChanged(selectedPosition)        // 更新当前选中的项
    }
}