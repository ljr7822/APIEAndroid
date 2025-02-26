package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.utils.APieCurrencyUtils
import com.xiaoxun.apie.common.utils.DateTimeUtils
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.storage.ThingItemModel
import com.xiaoxun.apie.home_page.adapter.thing.APieThingGroupAdapter
import com.xiaoxun.apie.home_page.adapter.thing.APieThingItemAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexStorageFragmentBinding
import com.xiaoxun.apie.home_page.repo.thing.IThingRepo
import com.xiaoxun.apie.home_page.repo.thing.ThingGroupSource
import com.xiaoxun.apie.home_page.repo.thing.ThingRepoImpl
import com.xiaoxun.apie.home_page.viewmodel.CommonLoadingState
import com.xiaoxun.apie.home_page.viewmodel.GenericViewModelFactory
import com.xiaoxun.apie.home_page.viewmodel.IndexStorageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class APieIndexStorageFragment : APieBaseBindingFragment<LayoutApieIndexStorageFragmentBinding>(
    LayoutApieIndexStorageFragmentBinding::inflate
) {

    companion object {
        private const val TAG = "APieIndexStorageFragment"
    }

    private val loadingDialog: APieLoadingDialog by lazy { APieLoadingDialog(hostActivity) }

    private val storageViewModel: IndexStorageViewModel by lazy {
        ViewModelProvider(
            hostActivity,
            GenericViewModelFactory { IndexStorageViewModel() })[IndexStorageViewModel::class.java]
    }

    private val repo: IThingRepo by lazy {
        ThingRepoImpl(storageViewModel)
    }

    private val thingGroupAdapter by lazy { APieThingGroupAdapter() }
    private val thingItemAdapter by lazy { APieThingItemAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initObserver()
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repo.loadThingGroups(ThingGroupSource.INDEX_PAGE)
            repo.loadThingList()
        }
    }

    private fun initView() {
        initRecycleView()
        initMineCardView()
    }

    private fun initMineCardView() {
        binding.assetsCount.text = APieCurrencyUtils.priceFormatting(price = AccountMMKVRepository.totalThingPrice)
        binding.assetsCountCount.text = AccountMMKVRepository.thingCount.toString()
        binding.timeTv.text = DateTimeUtils.formatDate()
    }

    private fun initObserver() {
        // 通用loading状态
        storageViewModel.commonLoadingState.observe(viewLifecycleOwner) {
            handleCommonLoadingState(
                it
            )
        }

        // 物品分组列表
        storageViewModel.thingGroupList.observe(viewLifecycleOwner) { newList ->
            // 更新数据
            val oldList = thingGroupAdapter.getItems()
            if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                // 滚动到前面
                binding.filterRecyclerView.scrollToPosition(0)
            }
            thingGroupAdapter.updateData(newList)
        }

        storageViewModel.currentThingList.observe(viewLifecycleOwner) { newList ->
            if (newList.isEmpty()) {
                binding.storageRecyclerView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.storageRecyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
                // 更新数据
                val oldList = thingItemAdapter.getItems()
                if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                    // 滚动到顶部
                    binding.storageRecyclerView.scrollToPosition(0)
                }
                thingItemAdapter.updateData(newList)
            }

            // 更新卡片数据
            updateCostCard(newList)
        }
    }

    private fun updateCostCard(thingList: List<ThingItemModel>) {
        val dailyCost = thingList.sumOf { it.getAveragePrice().toDouble() }
        binding.todayCostCount.text = APieCurrencyUtils.priceFormatting(price = dailyCost)
        binding.predictCostCount.text = APieCurrencyUtils.priceFormatting(price = dailyCost)
    }

    private fun handleCommonLoadingState(state: CommonLoadingState) {
        when (state) {
            CommonLoadingState.START -> loadingDialog.show()
            CommonLoadingState.SUCCESS -> loadingDialog.dismiss()
            CommonLoadingState.FAILED -> loadingDialog.dismiss()
        }
    }

    private fun initRecycleView() {
        binding.filterRecyclerView.apply {
            adapter = thingGroupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        thingItemAdapter.setItemClickListener(object : APieThingItemAdapter.ItemClickListener {
            override fun onItemClick(position: Int, thingItemModel: ThingItemModel) {
                // 点击事件
                APieToast.showDialog("点击了物品${thingItemModel.thingName}")
            }

            override fun onItemEditClick(position: Int, thingItemModel: ThingItemModel) {
                // 编辑事件
                APieToast.showDialog("编辑物品${thingItemModel.thingName}")
            }

            override fun onItemDeleteClick(position: Int, thingItemModel: ThingItemModel) {
                // 删除事件
                APieToast.showDialog("删除物品${thingItemModel.thingName}")
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    repo.deleteThing(thingItemModel.thingId)
                }
            }
        })

        binding.storageRecyclerView.apply {
            adapter = thingItemAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            // 列表停止滚动
                            storageViewModel.updateListScrolling(false)
                        }

                        RecyclerView.SCROLL_STATE_DRAGGING, RecyclerView.SCROLL_STATE_SETTLING -> {
                            // 列表正在滚动
                            storageViewModel.updateListScrolling(true)
                            thingItemAdapter.hideDesireMenuLayer()
                        }
                    }
                }
            })
        }
    }
}