package com.xiaoxun.apie.home_page.adapter.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadImage
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel

class APieGroupManagerAdapter(
    private val items: MutableList<PlanGroupModel> = mutableListOf()
) : RecyclerView.Adapter<APieGroupManagerAdapter.ViewHolder>() {

    private var listener: OnGroupManagerItemClickListener? = null

    fun getItems(): List<PlanGroupModel> {
        return items
    }

    // 更新数据的方法
    fun updateData(newList: List<PlanGroupModel>) {
        val diffCallback = APieGroupManagerDiffCallback(items, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    @MainThread
    fun replayData(newData: List<PlanGroupModel>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun setOnGroupManagerItemClickListener(listener: OnGroupManagerItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupName: TextView = view.findViewById(R.id.groupName)
        val groupIcon: ImageView = view.findViewById(R.id.groupIcon)
        val groupEditTv: TextView = view.findViewById(R.id.groupEditTv)
        val groupDeleteTv: TextView = view.findViewById(R.id.groupDeleteTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.apie_group_manager_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.groupName.text = item.groupName
        if (item.groupIcon.isEmpty().not()) {
            holder.groupIcon.loadImage(holder.itemView.context, item.groupIcon)
        } else {
            holder.groupIcon.loadImage(holder.itemView.context, R.drawable.apie_setting_group_manager_item_def_icon)
        }
        holder.groupEditTv.setDebouncingClickListener {
            val realPosition = holder.adapterPosition
            listener?.onGroupItemEditClick(realPosition, item)
        }
        holder.groupDeleteTv.setDebouncingClickListener {
            val realPosition = holder.adapterPosition
            listener?.onGroupItemDeleteClick(realPosition, item)
        }
    }

    interface OnGroupManagerItemClickListener {
        fun onGroupItemEditClick(position: Int, item: PlanGroupModel)
        fun onGroupItemDeleteClick(position: Int, item: PlanGroupModel)
    }
}