package com.xiaoxun.apie.home_page.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.GlideCircleImageView
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
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
        val planFrequencyItemLayout: View = view.findViewById(R.id.planFrequencyItemLayout)
        val planTypeIcon: GlideCircleImageView = view.findViewById(R.id.planTypeIcon)
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
        setSelectedPosition(holder, position)
        holder.planFrequencyName.setDebouncingClickListener {
            updateSelectedPosition(position)
            itemClick(position, item)
        }
        bindPlanTypeIcon(holder, item)
    }

    private fun setSelectedPosition(holder: ViewHolder, position: Int) {
        if (position == selectedPosition) {
            holder.planFrequencyItemLayout.setBackgroundResource(R.drawable.apie_frequency_plan_selected_bg)
            holder.planFrequencyName.setTextColor(holder.itemView.context.getColor(com.xiaoxun.apie.common.R.color.apie_color_6F94F4))
            holder.planFrequencyName.setTypeface(null, Typeface.BOLD)
        } else {
            holder.planFrequencyItemLayout.setBackgroundResource(R.drawable.apie_frequenct_plan_normal_bg)
            holder.planFrequencyName.setTextColor(holder.itemView.context.getColor(com.xiaoxun.apie.common.R.color.apieTheme_colorBlack_alpha_40))
            holder.planFrequencyName.setTypeface(null, Typeface.NORMAL) // 正常字体
        }
    }

    private fun bindPlanTypeIcon(holder: ViewHolder, item: PlanListType) {
        holder.planTypeIcon.show()
        val typeIcon = when (item.type) {
            PlanListType.SINGLE_PLAN.type -> {
                com.xiaoxun.apie.common.R.drawable.apie_plan_single_type_icon
            }

            PlanListType.TODAY_PLAN.type -> {
                com.xiaoxun.apie.common.R.drawable.apie_plan_today_type_icon
            }

            PlanListType.WEEK_PLAN.type -> {
                com.xiaoxun.apie.common.R.drawable.apie_plan_week_type_icon
            }

            PlanListType.MONTH_PLAN.type -> {
                com.xiaoxun.apie.common.R.drawable.apie_plan_month_type_icon
            }

            PlanListType.YEAR_PLAN.type -> {
                com.xiaoxun.apie.common.R.drawable.apie_plan_yera_type_icon
            }

            PlanListType.CYCLE_PLAN.type -> {
                com.xiaoxun.apie.common.R.drawable.apie_plan_cycle_type_icon
            }

            else -> {
                com.xiaoxun.apie.common.R.drawable.apie_plan_custom_type_icon
            }
        }
        holder.planTypeIcon.loadImage(typeIcon, 1)
    }

    fun updateSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition) // 更新之前选中的项
        notifyItemChanged(selectedPosition)        // 更新当前选中的项
    }
}