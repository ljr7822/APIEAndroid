package com.xiaoxun.apie.home_page.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel

class APiePlanDiffCallback(
    private val oldList: List<PlanModel>,
    private val newList: List<PlanModel>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].planId == newList[newItemPosition].planId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}