package com.xiaoxun.apie.home_page.adapter.desire

import androidx.recyclerview.widget.DiffUtil
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel

class APieDesireDiffCallback(
    private val oldList: List<DesireModel>,
    private val newList: List<DesireModel>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].desireId == newList[newItemPosition].desireId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}