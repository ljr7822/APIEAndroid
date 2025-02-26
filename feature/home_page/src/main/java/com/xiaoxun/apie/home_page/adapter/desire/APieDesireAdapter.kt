package com.xiaoxun.apie.home_page.adapter.desire

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.APieCircularProgressView
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadRoundCornerImage
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.home_page.R

class APieDesireAdapter : RecyclerView.Adapter<APieDesireAdapter.ViewHolder>() {

    private val items: MutableList<DesireModel> = mutableListOf()
    private var itemClickListener: ItemClickListener? = null
    private var currentlySelectedPosition: Int? = null // 当前显示 menuLayer 的位置

    fun getItems(): List<DesireModel> {
        return items
    }

    fun setItemClickListener(listener: ItemClickListener?) {
        itemClickListener = listener
    }

    // 更新数据的方法
    fun updateData(newList: List<DesireModel>) {
        val diffCallback = APieDesireDiffCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: ImageView = view.findViewById(R.id.coverImageView)
        val desireTitleView: TextView = view.findViewById(R.id.desireTitleView)
        val goldValue: TextView = view.findViewById(R.id.goldValue)
        val desireSoldScheduleLayout: View = view.findViewById(R.id.desireSoldScheduleLayout)
        val desireSoldProgress: APieCircularProgressView =
            view.findViewById(R.id.desireSoldProgress)
        val desireSoldProgressTip: TextView = view.findViewById(R.id.desireSoldProgressTip)
        val desireSoldOutIcon: ImageView = view.findViewById(R.id.desireSoldOutIcon)
        val desireBuyLayout: View = view.findViewById(R.id.desireBuyLayout)
        val maskingLayer: View = view.findViewById(R.id.maskingLayer)

        // 操作菜单
        val desireMenuLayer: View = view.findViewById(R.id.desireMenuLayer)
        val dataAnalysisIcon: ImageView = view.findViewById(R.id.dataAnalysisIcon)
        val resetIcon: ImageView = view.findViewById(R.id.resetIcon)
        val visibilityIcon: ImageView = view.findViewById(R.id.visibilityIcon)
        val editIcon: ImageView = view.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_home_desire_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.coverImageView.loadRoundCornerImage(holder.itemView.context, item.desireIcon)
        holder.desireTitleView.text = item.desireName
        holder.goldValue.text = item.desirePrice.toString()
        bindPlanSchedule(holder, item)
        // 控制 desireMenuLayer 的可见性
        if (position == currentlySelectedPosition) {
            holder.desireMenuLayer.show()
        } else {
            holder.desireMenuLayer.hide()
        }
        bindItemClick(holder, position, item)
    }

    @SuppressLint("SetTextI18n")
    private fun bindPlanSchedule(holder: ViewHolder, item: DesireModel) {
        if (item.desireTotalCount > 0 && item.desireExchangeCount >= 0) {
            holder.desireSoldScheduleLayout.visibility = View.VISIBLE
            holder.desireSoldProgress.setTotalCount(item.desireTotalCount)
            holder.desireSoldProgress.setCompletedCount(item.desireExchangeCount)
            setDoneStatus(holder, item)
        } else {
            holder.desireSoldScheduleLayout.visibility = View.GONE
        }
    }

    private fun setDoneStatus(holder: ViewHolder, item: DesireModel) {
        if (item.desireExchangeCount == item.desireTotalCount) {
            holder.desireSoldProgress.hide()
            holder.desireSoldProgressTip.hide()
            holder.desireSoldOutIcon.show()
            holder.maskingLayer.show()
        } else {
            holder.desireSoldProgress.show()
            holder.desireSoldProgressTip.show()
            holder.desireSoldOutIcon.hide()
            holder.desireSoldProgressTip.text =
                "${item.desireExchangeCount}/${item.desireTotalCount}"
            holder.maskingLayer.hide()
        }
    }

    private fun bindItemClick(holder: ViewHolder, position: Int, item: DesireModel) {
        holder.itemView.setDebouncingClickListener {
            hideDesireMenuLayer()
        }

        // 蒙层长按
        holder.maskingLayer.setOnLongClickListener {
            val realPosition = holder.adapterPosition
            showDesireMenuLayer(realPosition)
            itemClickListener?.onItemLongClick(realPosition, item)
            true
        }
        // item长按
        holder.itemView.setOnLongClickListener {
            val realPosition = holder.adapterPosition
            showDesireMenuLayer(realPosition)
            itemClickListener?.onItemLongClick(realPosition, item)
            true
        }
        holder.desireBuyLayout.setDebouncingClickListener {
            // 打卡
            itemClickListener?.onItemBuyClick(position, item)
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

    private fun showDesireMenuLayer(position: Int) {
        if (currentlySelectedPosition == position) return // 如果已经是当前项，直接返回

        // 记录上一个位置，并更新当前选中位置
        val previousPosition = currentlySelectedPosition
        currentlySelectedPosition = position

        // 刷新当前和之前的位置
        notifyItemChanged(previousPosition ?: -1)
        notifyItemChanged(position)
    }

    fun hideDesireMenuLayer() {
        currentlySelectedPosition?.let {
            notifyItemChanged(it)
            currentlySelectedPosition = null
        }
    }


    interface ItemClickListener {
        fun onItemLongClick(position: Int, desireModel: DesireModel) {}
        fun onItemBuyClick(position: Int, desireModel: DesireModel)
        fun onItemDataAnalysisClick(position: Int, desireModel: DesireModel)
        fun onItemResetClick(position: Int, desireModel: DesireModel)
        fun onItemVisibilityClick(position: Int, desireModel: DesireModel) {}
        fun onItemEditClick(position: Int, desireModel: DesireModel)
        fun onItemDeleteClick(position: Int, desireModel: DesireModel)
    }
}