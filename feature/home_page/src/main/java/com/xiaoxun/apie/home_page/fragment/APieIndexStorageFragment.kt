package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.home_page.adapter.storage.StorageGroupAdapter
import com.xiaoxun.apie.home_page.adapter.storage.StorageItemAdapter
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

    private val storageGroupAdapter by lazy { StorageGroupAdapter() }
    private val storageItemAdapter by lazy { StorageItemAdapter() }

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
    }

    private fun initObserver(){
        // 通用loading状态
        storageViewModel.commonLoadingState.observe(viewLifecycleOwner) { handleCommonLoadingState(it) }

        // 物品分组列表
        storageViewModel.thingGroupList.observe(viewLifecycleOwner) { newList ->
            // 更新数据
            val oldList = storageGroupAdapter.getItems()
            if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                // 滚动到前面
                binding.filterRecyclerView.scrollToPosition(0)
            }
            storageGroupAdapter.updateData(newList)
        }

        storageViewModel.currentThingList.observe(viewLifecycleOwner) { newList ->
            if (newList.isEmpty()) {
                binding.storageRecyclerView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.storageRecyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
                // 更新数据
                val oldList = storageItemAdapter.getItems()
                if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                    // 滚动到顶部
                    binding.storageRecyclerView.scrollToPosition(0)
                }
                storageItemAdapter.updateData(newList)
            }
        }
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
            adapter = storageGroupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.storageRecyclerView.apply {
            adapter = storageItemAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}