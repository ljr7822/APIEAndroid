package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.home_page.widget.APieLeftDrawerPopupView
import com.xiaoxun.apie.home_page.widget.APieFilterPartShadowPopupView
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.adapter.APiePlanAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexHomeFragmentBinding
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.repo.IndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.PlanStatus
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModelFactory
import com.xiaoxun.apie.home_page.viewmodel.PlanListType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class APieIndexHomeFragment: APieBaseBindingFragment<LayoutApieIndexHomeFragmentBinding>(LayoutApieIndexHomeFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    private val viewModel: IndexHomeViewModel by lazy {
        ViewModelProvider(hostActivity, IndexHomeViewModelFactory()).get(
            IndexHomeViewModel::class.java
        )
    }

    private val topFilterView: APieFilterPartShadowPopupView by lazy {
        APieFilterPartShadowPopupView(context = hostActivity, gravity = Gravity.END)
    }

    private val repo: IIndexHomeRepo by lazy { IndexHomeRepo(viewModel) }

    private val adapter: APiePlanAdapter by lazy { APiePlanAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
        initObserver()
    }

    override fun initTopBarView() {
        super.initTopBarView()
        binding.topBar.leftIconView.setDebouncingClickListener {
            showLeftDrawerPopupView()
        }
        binding.topBar.rightIconView.setDebouncingClickListener {
            showTopFilterView()
        }
    }

    private fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
            repo.loadPlanByType(PlanListType.ALL_PLAN)
        }
    }

    private fun initObserver() {
        viewModel.planTypeList.observe(viewLifecycleOwner) {
            // 更新数据
            adapter.replayData(it.second)
        }

        viewModel.planStatusList.observe(viewLifecycleOwner) {
            // 更新数据
            adapter.replayData(it.second)
        }


        // 根据状态进行筛选
        viewModel.planStatus.observe(viewLifecycleOwner) {
            GlobalScope.launch(Dispatchers.Main) {
                repo.loadPlanByStatus(it)
            }
        }

        // 根据类型进行筛选
        viewModel.filterPlanType.observe(viewLifecycleOwner) {
            GlobalScope.launch(Dispatchers.Main) {
                repo.loadPlanByType(it)
            }
        }
    }

    private fun initRecyclerView() {
        binding.planRecyclerView.layoutManager = LinearLayoutManager(context)
//        binding.planRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.planRecyclerView.adapter = adapter
        binding.planRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        // 列表停止滚动
                        viewModel.updateListScrolling(false)
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING, RecyclerView.SCROLL_STATE_SETTLING -> {
                        // 列表正在滚动
                        viewModel.updateListScrolling(true)
                    }
                }
            }
        })
    }

    private fun showTopFilterView() {
        XPopup.Builder(hostActivity)
            .atView(binding.topBar)
            .isViewMode(false)
            .popupPosition(PopupPosition.Bottom)
            .asCustom(topFilterView)
            .show()
    }

    private fun showLeftDrawerPopupView() {
        XPopup.Builder(hostActivity)
            .isDestroyOnDismiss(true)
            .isViewMode(true)
            .asCustom(APieLeftDrawerPopupView(hostActivity))
            .show()
    }
}