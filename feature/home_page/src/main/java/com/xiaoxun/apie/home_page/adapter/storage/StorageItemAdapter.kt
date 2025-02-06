package com.xiaoxun.apie.home_page.adapter.storage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadRoundCornerImage
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.home_page.R

class StorageItemAdapter(
    private val items: MutableList<ThingItemModel> = mutableListOf(),
    val itemClick: (Int, ThingItemModel) -> Unit = { _, _ -> } // 返回选中项的 position 和对应的数据项
) : RecyclerView.Adapter<StorageItemAdapter.ViewHolder>() {

    fun getItems(): List<ThingItemModel> = items

    fun updateData(newList: List<ThingItemModel>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: ImageView = view.findViewById(R.id.coverImageView)
        val thingName: TextView = view.findViewById(R.id.thingName)
        val priceTv: TextView = view.findViewById(R.id.priceTv)
        val averagePrice: TextView = view.findViewById(R.id.averagePrice)
        val thingTagLayout: View = view.findViewById(R.id.thingTagLayout)
        val thingTagName: TextView = view.findViewById(R.id.thingTagName)
        val buyDateTv: TextView = view.findViewById(R.id.buyDateTv)
        val warrantyStatusLayout: View = view.findViewById(R.id.warrantyStatusLayout)
        val warrantyStatusTv: TextView = view.findViewById(R.id.warrantyStatusTv)
        val useStatusLayout: View = view.findViewById(R.id.useStatusLayout)
        val useStatusTv: TextView = view.findViewById(R.id.useStatusTv)
        val useDays: TextView = view.findViewById(R.id.useDays)
        val useDaysTitle: TextView = view.findViewById(R.id.useDaysTitle)
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
        holder.priceTv.text = "¥${item.thingPrice}"
        holder.averagePrice.text = item.thingPrice.toString()
        holder.thingTagName.text = "数码手机"
        holder.buyDateTv.text = "2025/01/01"
        holder.warrantyStatusTv.text = "过保"
        holder.useStatusTv.text = "使用中"
        holder.useDays.text = item.useDays.toString()
        holder.itemView.setOnClickListener {
            itemClick(position, item)
        }
    }
}