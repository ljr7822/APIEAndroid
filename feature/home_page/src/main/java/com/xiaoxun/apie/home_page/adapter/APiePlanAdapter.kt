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
import com.xiaoxun.apie.home_page.viewmodel.PlanListType

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
        val planTypeIcon: GlideCircleImageView = view.findViewById(R.id.planTypeIcon)
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
        bindPlanType(holder, item)
    }

    private fun bindPlanType(holder: ViewHolder, item: PlanModel) {
        val typeIcon = when(item.planType) {
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
}