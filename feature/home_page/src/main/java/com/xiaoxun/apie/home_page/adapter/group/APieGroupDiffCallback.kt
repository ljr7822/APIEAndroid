package com.xiaoxun.apie.home_page.adapter.group

import androidx.recyclerview.widget.DiffUtil
import com.xiaoxun.apie.common_model.home_page.base.IBaseGroupModel
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel

class APieGroupDiffCallback(
    private val oldList: List<IBaseGroupModel>,
    private val newList: List<IBaseGroupModel>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.groupId == newItem.groupId && oldItem.groupName == newItem.groupName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.groupId == newItem.groupId && oldItem.groupName == newItem.groupName

    }
}