package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.xiaoxun.apie.home_page.bean.PlanModeInfo
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.ui.APieStyleExitTip
import com.xiaoxun.apie.common.utils.APieVibrateTool
import com.xiaoxun.apie.home_page.widget.APieLeftDrawerPopupView
import com.xiaoxun.apie.home_page.widget.APieFilterPartShadowPopupView
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.home_page.adapter.APiePlanAdapter
import com.xiaoxun.apie.home_page.bean.planModel2PlanModeInfo
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexHomeFragmentBinding
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.repo.IndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.CompletedCountOptType
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModelFactory
import com.xiaoxun.apie.home_page.viewmodel.PlanListType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class APieIndexHomeFragment :
    APieBaseBindingFragment<LayoutApieIndexHomeFragmentBinding>(LayoutApieIndexHomeFragmentBinding::inflate) {
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

    private var itemClickListener: APiePlanAdapter.ItemClickListener? = null

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
            // 更新数据到当前显示的列表
            viewModel.updateCurrentPlanList(it.second)
        }

        viewModel.planStatusList.observe(viewLifecycleOwner) {
            // 更新数据到当前显示的列表
            viewModel.updateCurrentPlanList(it.second)
        }

        viewModel.currentPlanList.observe(viewLifecycleOwner) { newList ->
            // 更新数据
            val oldList = adapter.getItems()
            if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                // 滚动到顶部
                binding.planRecyclerView.scrollToPosition(0)
            }
            adapter.updateData(newList)
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
                        adapter.hidePlanMenuLayer()
                    }
                }
            }
        })
        itemClickListener = object : APiePlanAdapter.ItemClickListener {
            override fun onItemDeleteClick(position: Int, planModel: PlanModel) {
                APieVibrateTool.device(50)
                APieStyleExitTip.show(
                    activity = hostActivity,
                    key = APieStyleExitTip.APIE_DELETE_PLAN_KEY,
                    actionCallback = {
                        adapter.hidePlanMenuLayer()
                        GlobalScope.launch(Dispatchers.Main) {
                            repo.deletePlan(planModel.planId)
                        }
                    })
            }

            override fun onItemEditClick(position: Int, planModel: PlanModel) {
                APieVibrateTool.device(50)
                APieCreateFragment(
                    repo = repo,
                    viewModel = viewModel,
                    planModelInfo = planModel.planModel2PlanModeInfo()
                ).show(parentFragmentManager, "create_plan")
                adapter.hidePlanMenuLayer()
            }

            override fun onItemDoneClick(position: Int, planModel: PlanModel) {
                APieVibrateTool.device(50)
                if (planModel.planCompletedCount >= planModel.planFrequency) {
                    APieToast.showDialog("今日打卡已经完毕，明天再来吧～")
                    return
                }
                GlobalScope.launch(Dispatchers.Main) {
                    repo.updatePlanCompletedCount(
                        CompletedCountOptType.INCREMENT.type,
                        planModel.planId
                    )
                }
            }

            override fun onItemDataAnalysisClick(position: Int, planModel: PlanModel) {
                APieVibrateTool.device(50)
                APieToast.showDialog("数据分析了第$position 个item")
            }

            override fun onItemResetClick(position: Int, planModel: PlanModel) {
                APieVibrateTool.device(50)
                if (planModel.planCompletedCount == 0) {
                    APieToast.showDialog("目前还没有打卡记录哦，无法撤销～")
                    return
                }
                APieStyleExitTip.show(
                    activity = hostActivity,
                    key = APieStyleExitTip.APIE_RESECT_PLAN_COUNT_KEY,
                    actionCallback = {
                        adapter.hidePlanMenuLayer()
                        GlobalScope.launch(Dispatchers.Main) {
                            repo.updatePlanCompletedCount(
                                CompletedCountOptType.DECREMENT.type,
                                planModel.planId
                            )
                        }
                    })
            }
        }
        adapter.setItemClickListener(itemClickListener)
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

    override fun onDestroy() {
        super.onDestroy()
        repo.onCleared()
    }
}