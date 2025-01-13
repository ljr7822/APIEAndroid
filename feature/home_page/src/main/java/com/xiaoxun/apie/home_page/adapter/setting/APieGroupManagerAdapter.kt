package com.xiaoxun.apie.home_page.adapter.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.ui.GlideCircleImageView
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel

class APieGroupManagerAdapter(
    private val items: MutableList<PlanGroupModel> = mutableListOf()
) : RecyclerView.Adapter<APieGroupManagerAdapter.ViewHolder>() {

    private var listener: OnGroupManagerItemClickListener? = null

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
        val groupIcon: GlideCircleImageView = view.findViewById(R.id.groupIcon)
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
            holder.groupIcon.loadImage(item.groupIcon, 1)
        } else {
            holder.groupIcon.loadImage(R.drawable.apie_setting_group_manager_item_def_icon, 1)
        }
        holder.groupEditTv.setDebouncingClickListener {
            listener?.onGroupItemEditClick(position, item)
        }
        holder.groupDeleteTv.setDebouncingClickListener {
            listener?.onGroupItemDeleteClick(position, item)
        }
    }

    interface OnGroupManagerItemClickListener {
        fun onGroupItemEditClick(position: Int, item: PlanGroupModel)
        fun onGroupItemDeleteClick(position: Int, item: PlanGroupModel)
    }
}