package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.loper7.date_time_picker.dialog.CardWeekPickerDialog
import com.loper7.date_time_picker.number_picker.NumberPicker
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.home_page.bean.PlanModeInfo
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.StringUtils
import com.xiaoxun.apie.common.utils.ThreadUtil
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.account.AccountManager
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toFormatList
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.home_page.R
import com.xiaoxun.apie.home_page.adapter.APiePlanFrequencyAdapter
import com.xiaoxun.apie.home_page.adapter.APiePlanGroupAdapter
import com.xiaoxun.apie.home_page.adapter.SpaceItemDecoration
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreatePlanFragmentBinding
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.CreatePlanState
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.LoadPlanGroupListState
import com.xiaoxun.apie.home_page.viewmodel.PlanListType
import com.xiaoxun.apie.home_page.viewmodel.TimeRangeType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class APieCreateFragment(
    private val repo: IIndexHomeRepo,
    private val viewModel: IndexHomeViewModel,
    private val planModelInfo: PlanModeInfo? = null
) : BottomSheetDialogFragment() {

    private var _binding: LayoutApieCreatePlanFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var frequencyAdapter: APiePlanFrequencyAdapter
    private lateinit var groupAdapter: APiePlanGroupAdapter

    private val isReedit: Boolean by lazy { planModelInfo != null }

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
        initReeditData()
    }

    private fun initDialogStyle() {
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
            setBackgroundResource(com.xiaoxun.apie.common.R.drawable.apie_create_plan_fragment_bg)
            val behavior = BottomSheetBehavior.from(this)
            val screenHeight = UIUtils.getScreenRealHeight(requireContext())
            behavior.peekHeight = screenHeight - 100.dp
            layoutParams.height = screenHeight - 100.dp
            behavior.isHideable = false
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {}
                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }

    private fun initReeditData() {
        planModelInfo?.let {
            binding.nameEdit.setText(it.planName)
            viewModel.updateSelectPlanFrequency(it.getPlanListTypeByPlanType())
            viewModel.updateSelectPlanGroup(it.belongGroupId)
            binding.frequencyCountEdit.setText(it.planFrequency.toString())
            binding.awardCountEdit.setText(it.planAward.toString())
            binding.deductCountEdit.setText(it.planPunish.toString())
            viewModel.updateSelectTimeRange(
                TimeRangeType.START_TIME,
                Pair(it.planStartTime, StringUtils.conversionTime(it.planStartTime, "yyyy-MM-dd"))
            )
            viewModel.updateSelectTimeRange(
                TimeRangeType.STOP_TIME,
                Pair(it.planStopTime, StringUtils.conversionTime(it.planStopTime, "yyyy-MM-dd"))
            )
        }
    }

    private fun initView() {
        initFrequencyRecyclerView()
        initGroupRecyclerView()
        initBottomView()
        binding.cancelLayout.setDebouncingClickListener { dismiss() }
        binding.submitLayout.setDebouncingClickListener {
            createOrReeditPlan()
        }
        binding.startTime.setDebouncingClickListener {
            showSelectDayView(
                TimeRangeType.START_TIME,
                viewModel.getSelectStartTime() ?: System.currentTimeMillis()
            )
        }
        binding.stopTime.setDebouncingClickListener {
            if (viewModel.getSelectPlanFrequency() == PlanListType.CYCLE_PLAN) {
                APieToast.showDialog("无限计划不需要设置结束时间")
            } else {
                showSelectDayView(
                    TimeRangeType.STOP_TIME,
                    viewModel.getSelectStopTime() ?: System.currentTimeMillis()
                )
            }
        }
    }

    private fun initBottomView() {
        if (isReedit) {
            binding.title.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_reedit_title)
            binding.cancelBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_reedit_cancel_btn_tip)
            binding.submitBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_reedit_submit_btn_tip)
        } else {
            binding.title.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_title)
            binding.cancelBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_cancel_btn_tip)
            binding.submitBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_submit_btn_tip)
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
            viewModel.updateSelectPlanFrequency(item)
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
        viewLifecycleOwner.lifecycleScope.launch {
            repo.loadPlanGroup()
        }
        viewModel.updateSelectTimeRange(
            TimeRangeType.START_TIME,
            Pair(System.currentTimeMillis(), getCurrentTimeStr())
        )
    }

    private fun initObserver() {
        // 分组列表加载状态
        viewModel.loadPlanGroupListState.observe(viewLifecycleOwner) { handleGroupLoadState(it) }
        // 计划分组列表
        viewModel.planGroupList.observe(viewLifecycleOwner) { groupAdapter.replayData(it) }
        // 创建计划状态
        viewModel.createPlanState.observe(viewLifecycleOwner) { handleCreatePlanState(it) }
        // 计划频率选择状态
        viewModel.selectPlanFrequency.observe(viewLifecycleOwner) { handleFrequencyChange(it) }
        // 计划分组选择状态
        viewModel.selectPlanGroup.observe(viewLifecycleOwner) { handlePlanGroupChange(it) }
        // 更新时间区间
        viewModel.selectTimeRange.observe(viewLifecycleOwner) { handleSelectTimeRangeChange(it) }
    }

    private fun handleGroupLoadState(state: LoadPlanGroupListState) {
        when (state) {
            LoadPlanGroupListState.START -> loadPlanGroupStart()
            LoadPlanGroupListState.SUCCESS -> loadPlanGroupSuccess()
            LoadPlanGroupListState.FAILED -> loadPlanGroupFailed()
        }
    }

    private fun handleCreatePlanState(state: CreatePlanState) {
        when (state) {
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

    /**
     * 计划类型变化后，调整开始和结束日期
     */
    private fun handleFrequencyChange(frequency: PlanListType?) {
        frequency ?: return
        binding.stopTime.text = if (isReedit && (planModelInfo?.planStopTime ?: 0) > 0) {
            StringUtils.conversionTime(planModelInfo?.planStopTime ?: 0, "yyyy-MM-dd")
        } else if (frequency == PlanListType.CYCLE_PLAN) {
            "无限期"
        } else {
            "请选择"
        }
        if (frequency == PlanListType.SINGLE_PLAN) {
            setFrequencyCountEditEnabled(false)
        } else {
            setFrequencyCountEditEnabled(true)
        }
        selectPlanType(frequency)
    }

    private fun handlePlanGroupChange(planGroupId: String) {
        selectGroupType(planGroupId)
    }

    /**
     * 选择时间范围变化后，调整开始和结束日期
     */
    private fun handleSelectTimeRangeChange(rangeMap: HashMap<TimeRangeType, Pair<Long, String>>) {
        binding.startTime.text = rangeMap[TimeRangeType.START_TIME]?.second ?: getCurrentTimeStr()
        binding.stopTime.text = rangeMap[TimeRangeType.STOP_TIME]?.second ?: "请选择"
    }

    private fun setFrequencyCountEditEnabled(enable: Boolean) {
        val colorRes = if (enable) com.xiaoxun.apie.common.R.color.apieTheme_colorBlack_alpha_80
        else com.xiaoxun.apie.common.R.color.apieTheme_colorBlack_alpha_20

        activity?.let {
            binding.frequencyOneTip.setTextColor(it.getColor(colorRes))
            binding.frequencyCountTip.setTextColor(it.getColor(colorRes))
            binding.frequencyCountEdit.isEnabled = enable
            if (!enable) binding.frequencyCountEdit.text.clear()
        }
    }

    private fun selectPlanType(planType: PlanListType) {
        frequencyAdapter.updateSelectedByPlanListType(planType)
        val index = frequencyAdapter.getItems().indexOfFirst { it == planType }
        binding.planFrequencyRv.scrollToPosition(index)
    }

    private fun selectGroupType(groupId: String) {
        ThreadUtil.runOnMainThreadDelay({
            groupAdapter.updateSelectedByGroupId(groupId)
            val index = groupAdapter.getItems().indexOfFirst { it.groupId == groupId }
            binding.planGroupRv.scrollToPosition(index)
        }, 400)

    }

    override fun getTheme(): Int {
        return com.xiaoxun.apie.common.R.style.APieBottomSheetAnimation
    }

    private fun createOrReeditPlan() {
        if (isReedit) {
            APieToast.showDialog("编辑计划")
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                repo.createPlan(buildCreatePlanRequestBody())
            }
        }
    }

    private fun buildCreatePlanRequestBody(): CreatePlanRequestBody {
        return CreatePlanRequestBody(
            planName = binding.nameEdit.text.toString(),
            planIcon = "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
            planType = selectedFrequency?.type ?: PlanListType.SINGLE_PLAN.type, // 默认是单次计划
            belongGroupId = selectedGroup?.groupId ?: "",
            planFrequency = binding.frequencyCountEdit.text.toString().toIntOrNull() ?: 1, // 计划频率
            planAward = binding.awardCountEdit.text.toString().toIntOrNull() ?: 1,// 奖励派币数量
            planPunish = binding.deductCountEdit.text.toString().toIntOrNull() ?: 0,// 扣除派币数量
            planWeight = 10,
            createUserId = AccountManager.getUserId(),
            planStartTime = viewModel.getSelectStartTime() ?: 0,
            planStopTime = viewModel.getSelectStopTime() ?: 0
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

    private fun showSelectDayView(timeRangeType: TimeRangeType, defaultTime: Long) {
        CardDatePickerDialog.builder(requireContext())
            .setTitle(timeRangeType.desc)
            .setDisplayType(mutableListOf(0, 1, 2))
            .setDefaultTime(defaultTime)
            .setBackGroundModel(2)
            .showBackNow(false)
            .setWrapSelectorWheel(false)
            .setThemeColor(requireContext().getColor(com.xiaoxun.apie.common.R.color.apie_color_6F94F4))
            .showDateLabel(false)
            .showFocusDateInfo(false)
            .setOnChoose("确定") { millisecond ->
                val showText = StringUtils.conversionTime(millisecond, "yyyy-MM-dd")
                viewModel.updateSelectTimeRange(timeRangeType, Pair(millisecond, showText))
                if (timeRangeType == TimeRangeType.START_TIME) {
                    binding.startTime.text = showText
                } else if (timeRangeType == TimeRangeType.STOP_TIME) {
                    binding.stopTime.text = showText
                }
            }
            .setOnCancel("取消") {}
            .build().show()
    }

    private fun showSelectTimePeriodView(type: PlanListType) {
        if (type == PlanListType.WEEK_PLAN) {
            CardWeekPickerDialog.builder(requireContext())
                .setTitle("选择计划有效周期")
                .setBackGroundModel(2)
                .setWrapSelectorWheel(false)
                .setThemeColor(requireContext().getColor(com.xiaoxun.apie.common.R.color.apie_color_6F94F4))
                .setFormatter {//设置数据格式化样式
                    NumberPicker.Formatter { value: Int ->
                        val weekData = it[value - 1].toFormatList("yyyy年MM月dd日")
                        val str = "${weekData.first()}-${weekData.last()}"
                        str
                    }
                }
                .setOnChoose("确定") { weekData, formatValue ->
                    APieLog.i("ljrxxx", "weekData:$weekData, formatValue:$formatValue")
                    //选择按钮回调监听
                    binding.startTime.text = formatValue
                }
                .setOnCancel("取消") {
                    //关闭按钮回调监听
                }.build().show()
        }
    }

    private fun getCurrentTimeStr(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetCreatePlanStatus()
    }
}