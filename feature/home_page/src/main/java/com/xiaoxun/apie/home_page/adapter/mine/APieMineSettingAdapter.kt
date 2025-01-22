package com.xiaoxun.apie.home_page.adapter.mine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.APieSwitch
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.mine.MineSettingInfo
import com.xiaoxun.apie.common_model.home_page.mine.MineSettingItemType
import com.xiaoxun.apie.home_page.R

class APieMineSettingAdapter(
    private val items: MutableList<MineSettingInfo> = mutableListOf(),
): RecyclerView.Adapter<APieMineSettingAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    @MainThread
    fun replayData(newData: List<MineSettingInfo>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leftIcon: ImageView = view.findViewById(R.id.leftIcon)
        val title: TextView = view.findViewById(R.id.itemTitle)
        val rightIcon: ImageView = view.findViewById(R.id.rightIcon)
        val switchCompat: APieSwitch = view.findViewById(R.id.switchCompat)
        val itemTip: TextView = view.findViewById(R.id.itemTip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_mine_setting_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.leftIcon.setImageResource(item.itemLeftIcon)
        holder.title.text = item.itemTitle

        holder.rightIcon.visibility = if (item.itemType == MineSettingItemType.ITEM_TYPE_NORMAL) View.VISIBLE else View.GONE
        holder.switchCompat.visibility = if (item.itemType == MineSettingItemType.ITEM_TYPE_SWITCH) View.VISIBLE else View.GONE

        if (item.itemType == MineSettingItemType.ITEM_TYPE_TIP) {
            holder.itemTip.visibility = View.VISIBLE
            holder.itemTip.text = item.itemTip
        } else {
            holder.itemTip.visibility = View.GONE
        }

        holder.itemView.setDebouncingClickListener {
            itemClickListener?.itemClick(position, item)
        }
    }

    interface OnItemClickListener {
        fun itemClick(position: Int, item: MineSettingInfo)
    }
}