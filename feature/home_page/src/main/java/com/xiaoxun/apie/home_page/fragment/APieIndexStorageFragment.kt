package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common_model.home_page.storage.StorageGroupModel
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.home_page.adapter.storage.StorageGroupAdapter
import com.xiaoxun.apie.home_page.adapter.storage.StorageItemAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexStorageFragmentBinding

class APieIndexStorageFragment : APieBaseBindingFragment<LayoutApieIndexStorageFragmentBinding>(
    LayoutApieIndexStorageFragmentBinding::inflate
) {

    private val storageGroupAdapter by lazy { StorageGroupAdapter() }
    private val storageItemAdapter by lazy { StorageItemAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mockData()
        initView()
    }

    private fun initView() {
        initRecycleView()
    }

    private fun initRecycleView() {

        binding.filterRecyclerView.apply {
            adapter = storageGroupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.storageRecyclerView.apply {
            adapter = storageItemAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun mockData() {
        val groups = mutableListOf(
            StorageGroupModel(
                groupId = "1",
                createUserId = "1",
                groupName = "全部",
                groupIcon = "https://img1)"
            ),
            StorageGroupModel(
                groupId = "1",
                createUserId = "1",
                groupName = "数码电子",
                groupIcon = "https://img1)"
            ),StorageGroupModel(
                groupId = "1",
                createUserId = "1",
                groupName = "服装",
                groupIcon = "https://img1)"
            ),StorageGroupModel(
                groupId = "1",
                createUserId = "1",
                groupName = "生活用品",
                groupIcon = "https://img1)"
            ),StorageGroupModel(
                groupId = "1",
                createUserId = "1",
                groupName = "美妆护肤",
                groupIcon = "https://img1)"
            ),StorageGroupModel(
                groupId = "1",
                createUserId = "1",
                groupName = "食品饮料",
                groupIcon = "https://img1)"
            )
        )
        storageGroupAdapter.updateData(groups)

        val things = mutableListOf(
            ThingItemModel(
                thingId = "1",
                thingIcon = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
                thingName = "小米11",
                thingPrice = 3999f,
                thingTags = mutableListOf(),
                thingStatus = 1,
                useDays = 100
            ),
            ThingItemModel(
                thingId = "1",
                thingIcon = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
                thingName = "小米11",
                thingPrice = 3999f,
                thingTags = mutableListOf(),
                thingStatus = 1,
                useDays = 100
            ),
            ThingItemModel(
                thingId = "1",
                thingIcon = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
                thingName = "小米11",
                thingPrice = 3999f,
                thingTags = mutableListOf(),
                thingStatus = 1,
                useDays = 100
            ),
            ThingItemModel(
                thingId = "1",
                thingIcon = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
                thingName = "小米11",
                thingPrice = 3999f,
                thingTags = mutableListOf(),
                thingStatus = 1,
                useDays = 100
            ),
            ThingItemModel(
                thingId = "1",
                thingIcon = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
                thingName = "小米11",
                thingPrice = 3999f,
                thingTags = mutableListOf(),
                thingStatus = 1,
                useDays = 100
            ),
            ThingItemModel(
                thingId = "1",
                thingIcon = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
                thingName = "小米11",
                thingPrice = 3999f,
                thingTags = mutableListOf(),
                thingStatus = 1,
                useDays = 100
            ),
            ThingItemModel(
                thingId = "1",
                thingIcon = "https://imqingliao.oss-cn-beijing.aliyuncs.com/avatar/202403/7f445054bc976a26ecaa7ea7be0d1726.jpg",
                thingName = "小米11",
                thingPrice = 3999f,
                thingTags = mutableListOf(),
                thingStatus = 1,
                useDays = 100
            )
        )
        storageItemAdapter.updateData(things)
    }
}