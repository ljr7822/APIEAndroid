package com.xiaoxun.apie.home_page.adapter.thing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common_model.home_page.storage.StorageStatusModel
import com.xiaoxun.apie.home_page.R

class APieThingStatusAdapter(
    private val items: MutableList<StorageStatusModel> = mutableListOf(),
    val itemClick: (Int, StorageStatusModel) -> Unit = { _, _ -> } // 返回选中项的 position 和对应的数据项
): RecyclerView.Adapter<APieThingStatusAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1 // 记录当前选中的位置

    fun getItems(): List<StorageStatusModel> {
        return items
    }

    fun updateData(newList: List<StorageStatusModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    fun getSelectedStatus(): StorageStatusModel? {
        return if (selectedPosition != -1) {
            items[selectedPosition]
        } else {
            null
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusLayout: View = view.findViewById(R.id.useStatusLayout)
        val statusName: TextView = view.findViewById(R.id.useStatusText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_apie_storage_status_item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.statusName.text = item.statusType.statusName
        setSelectedPosition(holder, position)
        holder.statusLayout.setOnClickListener {
            updateSelectedPosition(position)
            itemClick(position, item)
        }
    }

    private fun updateSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition) // 更新之前选中的项
        notifyItemChanged(selectedPosition)        // 更新当前选中的项
    }

    private fun setSelectedPosition(holder: ViewHolder, position: Int) {
        if (position == selectedPosition) {
            holder.statusName.setBackgroundResource(com.xiaoxun.apie.common.R.drawable.apie_storage_group_select_btn_bg)
            holder.statusName.setTextColor(holder.itemView.context.getColor(com.xiaoxun.apie.common.R.color.apie_color_4d6bfe))
        } else {
            holder.statusName.setBackgroundResource(com.xiaoxun.apie.common.R.drawable.apie_storage_group_def_btn_bg)
            holder.statusName.setTextColor(holder.itemView.context.getColor(com.xiaoxun.apie.common.R.color.apieTheme_colorBlack_alpha_40))
        }
    }
}