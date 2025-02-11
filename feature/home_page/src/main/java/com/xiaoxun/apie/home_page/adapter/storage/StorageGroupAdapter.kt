package com.xiaoxun.apie.home_page.adapter.storage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.base.IBaseGroupModel

/**
 * 资产分组适配器
 */
class StorageGroupAdapter(
    private val items: MutableList<IBaseGroupModel> = mutableListOf(),
    val itemClick: (Int, IBaseGroupModel) -> Unit = { _, _ -> } // 返回选中项的 position 和对应的数据项
) : RecyclerView.Adapter<StorageGroupAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1 // 记录当前选中的位置

    fun getItems(): List<IBaseGroupModel> {
        return items
    }

    // 更新数据的方法
    fun updateData(newList: List<IBaseGroupModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupName: TextView = view.findViewById(R.id.storageGroupName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.apie_storage_group_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.groupName.text = item.groupName
        setSelectedPosition(holder, position)
        holder.groupName.setDebouncingClickListener {
            updateSelectedPosition(position)
            itemClick(position, item)
        }
    }

    private fun setSelectedPosition(holder: ViewHolder, position: Int) {
        if (position == selectedPosition) {
            holder.groupName.setBackgroundResource(R.drawable.apie_storage_group_select_btn_bg)
            holder.groupName.setTextColor(holder.itemView.context.getColor(R.color.apie_color_4d6bfe))
        } else {
            holder.groupName.setBackgroundResource(R.drawable.apie_storage_group_def_btn_bg)
            holder.groupName.setTextColor(holder.itemView.context.getColor(R.color.apieTheme_colorBlack))
        }
    }

    private fun updateSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition) // 更新之前选中的项
        notifyItemChanged(selectedPosition)        // 更新当前选中的项
    }

    override fun getItemCount(): Int = items.size

}