package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.account.AccountManager
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.home_page.adapter.APiePlanFrequencyAdapter
import com.xiaoxun.apie.home_page.adapter.APiePlanGroupAdapter
import com.xiaoxun.apie.home_page.adapter.SpaceItemDecoration
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreatePlanFragmentBinding
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.CreatePlanState
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.LoadPlanGroupListState
import com.xiaoxun.apie.home_page.viewmodel.PlanListType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class APieCreateFragment(
    private val repo: IIndexHomeRepo,
    private val viewModel: IndexHomeViewModel
) : BottomSheetDialogFragment() {

    private var _binding: LayoutApieCreatePlanFragmentBinding? = null
    private val binding get() = _binding!!

    private var frequencyAdapter: APiePlanFrequencyAdapter? = null
    private var groupAdapter: APiePlanGroupAdapter? = null

    private var selectedFrequency: PlanListType? = null
    private var selectedGroup: PlanGroupModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutApieCreatePlanFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialogStyle()
        initData()
        initObserver()
        initView()
    }

    private fun initDialogStyle() {
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            it.setBackgroundResource(com.xiaoxun.apie.common.R.drawable.apie_create_plan_fragment_bg) // 设置圆角背景

            val behavior = BottomSheetBehavior.from(it)
            val screenHeight = UIUtils.getScreenRealHeight(requireContext())
            behavior.peekHeight = screenHeight - 50.dp
            it.layoutParams.height = screenHeight - 50.dp

            // 禁止隐藏
            behavior.isHideable = false

            // 设置拖拽时的回调
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // 状态切换
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // 处理滑动事件
                }
            })
        }
    }

    private fun initView() {
        initFrequencyRecyclerView()
        initGroupRecyclerView()
        binding.cancelLayout.setDebouncingClickListener {
            dismiss()
        }
        binding.submitLayout.setDebouncingClickListener {
            createPlan()
        }
    }

    private fun initFrequencyRecyclerView() {
        val frequencyList = mutableListOf(
            PlanListType.SINGLE_PLAN,
            PlanListType.TODAY_PLAN,
            PlanListType.WEEK_PLAN,
            PlanListType.MONTH_PLAN,
            PlanListType.YEAR_PLAN,
            PlanListType.CUSTOM_PLAN,
            PlanListType.CYCLE_PLAN
        )
        frequencyAdapter = APiePlanFrequencyAdapter(items = frequencyList) { pos, item ->
            selectedFrequency = item
        }

        binding.planFrequencyRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = frequencyAdapter
            addItemDecoration(SpaceItemDecoration())
        }
    }

    private fun initGroupRecyclerView() {
        groupAdapter = APiePlanGroupAdapter { pos, item ->
            selectedGroup = item
        }

        binding.planGroupRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
            addItemDecoration(SpaceItemDecoration())
        }

    }

    private fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
            repo.loadPlanGroup()
        }
    }

    private fun initObserver() {
        viewModel.loadPlanGroupListState.observe(viewLifecycleOwner) {
            when (it) {
                LoadPlanGroupListState.START -> {
                    loadPlanGroupStart()
                }

                LoadPlanGroupListState.SUCCESS -> {
                    loadPlanGroupSuccess()
                }

                LoadPlanGroupListState.FAILED -> {
                    loadPlanGroupFailed()
                }

                else -> {}
            }
        }
        viewModel.planGroupList.observe(viewLifecycleOwner) {
            groupAdapter?.replayData(it)
        }

        viewModel.createPlanState.observe(viewLifecycleOwner) {
            when (it) {
                CreatePlanState.START -> {
                    // 开始创建计划
                    APieToast.showDialog("开始创建计划")
                }

                CreatePlanState.SUCCESS -> {
                    // 创建计划成功
                    APieToast.showDialog("创建计划成功")
                    dismiss()
                }

                CreatePlanState.FAILED -> {
                    // 创建计划失败
                    APieToast.showDialog("创建计划失败")
                }

                else -> {}
            }
        }
    }

    override fun getTheme(): Int {
        return com.xiaoxun.apie.common.R.style.APieBottomSheetAnimation
    }

    private fun createPlan() {
        GlobalScope.launch(Dispatchers.Main) {
            repo.createPlan(buildCreatePlanRequestBody())
        }
    }

    private fun buildCreatePlanRequestBody(): CreatePlanRequestBody {
        return CreatePlanRequestBody(
            planName = binding.nameEdit.text.toString(),
            planIcon = "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
            planType = selectedFrequency?.type ?: PlanListType.SINGLE_PLAN.type, // 默认是单次计划
            belongGroupId = selectedGroup?.groupId ?: "",
            planFrequency = binding.frequencyCountEdit.text.toString(), // 计划频率
            planAward = binding.awardCountEdit.text.toString().toIntOrNull() ?: 1,// 奖励派币数量
            planPunish = binding.deductCountEdit.text.toString().toIntOrNull() ?: 0,// 扣除派币数量
            planWeight = 10,
            createUserId = AccountManager.getUserId()
        )
    }

    private fun loadPlanGroupStart() {
        binding.planGroupRv.visibility = View.GONE
        binding.groupLoadingLayout.visibility = View.VISIBLE
        binding.groupLoadingView.show()
    }

    private fun loadPlanGroupSuccess() {
        binding.planGroupRv.visibility = View.VISIBLE
        binding.groupLoadingLayout.visibility = View.GONE
        binding.groupLoadingView.hide()
    }

    private fun loadPlanGroupFailed() {
        binding.planGroupRv.visibility = View.GONE
        binding.groupLoadingLayout.visibility = View.GONE
        binding.groupErrorTips.visibility = View.VISIBLE
        binding.groupLoadingView.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetCreatePlanStatus()
    }
}