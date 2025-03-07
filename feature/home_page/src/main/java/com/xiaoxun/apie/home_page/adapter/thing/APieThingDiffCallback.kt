package com.xiaoxun.apie.home_page.adapter.thing

import androidx.recyclerview.widget.DiffUtil
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel

class APieThingDiffCallback(
    private val oldList: List<ThingItemModel>,
    private val newList: List<ThingItemModel>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].thingId == newList[newItemPosition].thingId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}