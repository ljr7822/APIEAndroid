package com.xiaoxun.apie.home_page.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.GlideCircleImageView
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.home_page.R

class APiePlanAdapter(
    private val items: MutableList<PlanModel> = mutableListOf()
): RecyclerView.Adapter<APiePlanAdapter.ViewHolder> () {

    @MainThread
    fun replayData(newData: List<PlanModel>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val coverImageView: GlideCircleImageView = view.findViewById(R.id.coverImageView)
        val planTitleView: TextView = view.findViewById(R.id.planTitleView)
        val planGroupName: TextView = view.findViewById(R.id.planGroupName)
        val planFrequency: TextView = view.findViewById(R.id.planFrequency)
        val goldValue: TextView = view.findViewById(R.id.goldValue)
        val groupIcon: GlideCircleImageView = view.findViewById(R.id.groupIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_home_plan_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.planTitleView.text = item.planName
        holder.coverImageView.loadImage(item.planIcon, 20)
        holder.goldValue.text = item.planAward.toString()
        holder.planGroupName.text = item.planGroupModel.groupName
        holder.planFrequency.text = "每天${item.planFrequency}次"
        holder.groupIcon.loadImage(item.planGroupModel.groupIcon)
    }
}