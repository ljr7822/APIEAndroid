package com.xiaoxun.apie.home_page.adapter.thing

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadRoundCornerImage
import com.xiaoxun.apie.common.utils.APieCurrencyUtils
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.home_page.R
import com.xiaoxun.apie.home_page.adapter.desire.APieDesireAdapter.ItemClickListener

class APieThingItemAdapter(
    private val items: MutableList<ThingItemModel> = mutableListOf()
) : RecyclerView.Adapter<APieThingItemAdapter.ViewHolder>() {

    private var itemClickListener: ItemClickListener? = null
    private var currentlySelectedPosition: Int? = null // 当前显示 menuLayer 的位置

    fun getItems(): List<ThingItemModel> = items

    fun setItemClickListener(listener: ItemClickListener?) {
        itemClickListener = listener
    }

    fun resectData(newList: List<ThingItemModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    // 更新数据的方法
    fun updateData(newList: List<ThingItemModel>) {
        val diffCallback = APieThingDiffCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: ImageView = view.findViewById(R.id.coverImageView)
        val thingName: TextView = view.findViewById(R.id.thingName)
        val priceTv: TextView = view.findViewById(R.id.priceTv)
        val averagePrice: TextView = view.findViewById(R.id.averagePrice)
        val thingTagLayout: View = view.findViewById(R.id.thingTagLayout)
        val thingTagName: TextView = view.findViewById(R.id.thingTagName)
        val thingGroupLayout: View = view.findViewById(R.id.thingGroupLayout)
        val thingGroupName: TextView = view.findViewById(R.id.thingGroupName)
        val buyDateTv: TextView = view.findViewById(R.id.buyDateTv)
        val warrantyStatusLayout: View = view.findViewById(R.id.warrantyStatusLayout)
        val warrantyStatusTv: TextView = view.findViewById(R.id.warrantyStatusTv)
        val useStatusLayout: View = view.findViewById(R.id.useStatusLayout)
        val useStatusTv: TextView = view.findViewById(R.id.useStatusTv)
        val useDays: TextView = view.findViewById(R.id.useDays)
        val useDaysTitle: TextView = view.findViewById(R.id.useDaysTitle)
        val thingMenuLayer: View = view.findViewById(R.id.thingMenuLayer)
        val editIcon: View = view.findViewById(R.id.editIcon)
        val deleteIcon: View = view.findViewById(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apie_storage_thing_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.coverImageView.loadRoundCornerImage(holder.itemView.context, item.thingIcon)
        holder.thingName.text = item.thingName
        holder.priceTv.text = APieCurrencyUtils.priceFormatting(price = item.thingPrice.toDouble())
        holder.averagePrice.text = item.getAveragePriceString()
        holder.thingGroupName.text = item.thingGroupBean.groupName
        holder.buyDateTv.text = item.getBuyAtDesc()
        holder.warrantyStatusTv.text = item.checkWarrantyStatus()
        holder.useStatusTv.text = item.getThingStatusDesc()
        holder.useDays.text = item.daysSince().toString()
        // 控制 desireMenuLayer 的可见性
        if (position == currentlySelectedPosition) {
            holder.thingMenuLayer.show()
        } else {
            holder.thingMenuLayer.hide()
        }
        bindItemClick(holder, position, item)
    }

    private fun bindItemClick(holder: ViewHolder, position: Int, item: ThingItemModel) {
        holder.itemView.setDebouncingClickListener {
            hideDesireMenuLayer()
        }

        // 蒙层长按
        holder.thingMenuLayer.setOnLongClickListener {
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

        holder.editIcon.setDebouncingClickListener {
            itemClickListener?.onItemEditClick(position, item)
        }

        holder.deleteIcon.setDebouncingClickListener {
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
        fun onItemClick(position: Int, thingItemModel: ThingItemModel)
        fun onItemLongClick(position: Int, thingItemModel: ThingItemModel) {}
        fun onItemEditClick(position: Int, thingItemModel: ThingItemModel)
        fun onItemDeleteClick(position: Int, thingItemModel: ThingItemModel)
    }
}