package com.xiaoxun.apie.home_page.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.APieCircularProgressView
import com.xiaoxun.apie.common.ui.GlideCircleImageView
import com.xiaoxun.apie.common.utils.alphaHide
import com.xiaoxun.apie.common.utils.alphaShow
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.home_page.R
import com.xiaoxun.apie.home_page.viewmodel.PlanListType

class APiePlanAdapter : RecyclerView.Adapter<APiePlanAdapter.ViewHolder>() {
    private val items: MutableList<PlanModel> = mutableListOf()
    private var itemClickListener: ItemClickListener? = null
    private var currentlySelectedPosition: Int? = null // 当前显示 planMenuLayer 的位置

    fun getItems(): MutableList<PlanModel> = items

    // 更新数据的方法
    fun updateData(newList: List<PlanModel>) {
        val diffCallback = APiePlanDiffCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    // 插入单个数据项
    @MainThread
    fun insertItem(newItem: PlanModel, position: Int = 0) {
        if (position in 0..items.size) {
            items.add(position, newItem)
            notifyItemInserted(position)
        }
    }

    // 更新单个数据项
    @MainThread
    fun updateItem(newItem: PlanModel) {
        val position = items.indexOfFirst { it.planId == newItem.planId }
        if (position != -1) {
            items[position] = newItem
            notifyItemChanged(position)
        }
    }

    // 删除单个数据项
    @MainThread
    fun removeItem(item: PlanModel) {
        val position = items.indexOfFirst { it.planId == item.planId }
        if (position != -1) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: GlideCircleImageView = view.findViewById(R.id.coverImageView)
        val planTitleView: TextView = view.findViewById(R.id.planTitleView)
        val planGroupName: TextView = view.findViewById(R.id.planGroupName)
        val planEditView: ImageView = view.findViewById(R.id.planEdit)
        val planDeleteView: ImageView = view.findViewById(R.id.planDelete)
        val planScheduleLayout: View = view.findViewById(R.id.planScheduleLayout)
        val planScheduleTip: TextView = view.findViewById(R.id.planScheduleTip)
        val planScheduleProgress: APieCircularProgressView =
            view.findViewById(R.id.planScheduleProgress)
        val planDoneIcon: ImageView = view.findViewById(R.id.planDoneIcon)
        val planDoneClickIcon: View = view.findViewById(R.id.planDoneClickIcon)

        // 操作菜单
        val planMenuLayer: View = view.findViewById(R.id.planMenuLayer)
        val dataAnalysisIcon: ImageView = view.findViewById(R.id.dataAnalysisIcon)
        val resetIcon: ImageView = view.findViewById(R.id.resetIcon)
        val visibilityIcon: ImageView = view.findViewById(R.id.visibilityIcon)
        val editIcon: ImageView = view.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)
        val rightDoingIcon: ImageView = view.findViewById(R.id.rightDoingIcon)

        val maskingLayer: View = view.findViewById(R.id.maskingLayer)
        val goldValue: TextView = view.findViewById(R.id.goldValue)
        val planTypeIcon: GlideCircleImageView = view.findViewById(R.id.planTypeIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_home_plan_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.planTitleView.text = item.planName
        holder.coverImageView.loadImage(item.planIcon, 20)
        holder.goldValue.text = item.planAward.toString()
        holder.planGroupName.text = item.planGroupModel.groupName
        bindPlanSchedule(holder, item)
        bindPlanType(holder, item)
        // 控制 planMenuLayer 的可见性
        if (position == currentlySelectedPosition) {
            holder.planMenuLayer.show()
        } else {
            holder.planMenuLayer.hide()
        }
        bindItemClick(holder, position, item)
    }

    @SuppressLint("SetTextI18n")
    private fun bindPlanSchedule(holder: ViewHolder, item: PlanModel) {
        if (item.planFrequency > 0 && item.planCompletedCount >= 0) {
            holder.planScheduleLayout.visibility = View.VISIBLE
            holder.planScheduleProgress.setTotalCount(item.planFrequency)
            holder.planScheduleProgress.setCompletedCount(item.planCompletedCount)
            setDoneStatus(holder, item)
        } else {
            holder.planScheduleLayout.visibility = View.GONE
        }
    }

    private fun setDoneStatus(holder: ViewHolder, item: PlanModel) {
        if (item.planCompletedCount == item.planFrequency) {
            holder.planScheduleProgress.hide()
            holder.planScheduleTip.hide()
            holder.planDoneIcon.show()
            holder.maskingLayer.show()
            holder.rightDoingIcon.hide()
        } else {
            holder.planScheduleProgress.show()
            holder.planScheduleTip.show()
            holder.planDoneIcon.hide()
            holder.planScheduleTip.text = "${item.planCompletedCount}/${item.planFrequency}"
            holder.maskingLayer.hide()
            holder.rightDoingIcon.show()
        }
    }

    private fun bindPlanType(holder: ViewHolder, item: PlanModel) {
        val typeIcon = when (item.planType) {
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

    private fun bindItemClick(holder: ViewHolder, position: Int, item: PlanModel) {
        holder.itemView.setDebouncingClickListener {
            hidePlanMenuLayer()
        }
        // 蒙层长按
        holder.maskingLayer.setOnLongClickListener {
            showPlanMenuLayer(position)
            itemClickListener?.onItemLongClick(position, item)
            true
        }
        // item长按
        holder.itemView.setOnLongClickListener {
            showPlanMenuLayer(position)
            itemClickListener?.onItemLongClick(position, item)
            true
        }
        holder.planDoneClickIcon.setDebouncingClickListener {
            // 打卡
            itemClickListener?.onItemDoneClick(position, item)
        }
        // 菜单区域
        holder.dataAnalysisIcon.setDebouncingClickListener {
            // 数据分析
            itemClickListener?.onItemDataAnalysisClick(position, item)
        }
        holder.resetIcon.setDebouncingClickListener {
            // 重置
            itemClickListener?.onItemResetClick(position, item)
        }
        holder.visibilityIcon.setDebouncingClickListener {
            // 可见性
            itemClickListener?.onItemVisibilityClick(position, item)
        }
        holder.editIcon.setDebouncingClickListener {
            // 编辑
            itemClickListener?.onItemEditClick(position, item)
        }
        holder.deleteIcon.setDebouncingClickListener {
            // 删除
            itemClickListener?.onItemDeleteClick(position, item)
        }
    }

    private fun showPlanMenuLayer(position: Int) {
        if (currentlySelectedPosition == position) return // 如果已经是当前项，直接返回

        // 记录上一个位置，并更新当前选中位置
        val previousPosition = currentlySelectedPosition
        currentlySelectedPosition = position

        // 刷新当前和之前的位置
        notifyItemChanged(previousPosition ?: -1)
        notifyItemChanged(position)
    }

    fun hidePlanMenuLayer() {
        if (currentlySelectedPosition != null) {
            val previousPosition = currentlySelectedPosition
            currentlySelectedPosition = null
            notifyItemChanged(previousPosition!!)
        }
    }

    interface ItemClickListener {
        fun onItemLongClick(position: Int, planModel: PlanModel) {}
        fun onItemDoneClick(position: Int, planModel: PlanModel)
        fun onItemDataAnalysisClick(position: Int, planModel: PlanModel)
        fun onItemResetClick(position: Int, planModel: PlanModel)
        fun onItemVisibilityClick(position: Int, planModel: PlanModel) {}
        fun onItemEditClick(position: Int, planModel: PlanModel)
        fun onItemDeleteClick(position: Int, planModel: PlanModel)
    }
}