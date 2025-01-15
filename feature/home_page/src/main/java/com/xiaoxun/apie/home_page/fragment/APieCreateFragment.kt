package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.loper7.date_time_picker.dialog.CardWeekPickerDialog
import com.loper7.date_time_picker.number_picker.NumberPicker
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.ui.setEditTextMaxInput
import com.xiaoxun.apie.home_page.bean.PlanModeInfo
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.DateTimeUtils
import com.xiaoxun.apie.common.utils.ThreadUtil
import com.xiaoxun.apie.common.utils.UnitType
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toFormatList
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.home_page.adapter.APiePlanFrequencyAdapter
import com.xiaoxun.apie.home_page.adapter.group.APieGroupAdapter
import com.xiaoxun.apie.home_page.adapter.SpaceItemDecoration
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreatePlanFragmentBinding
import com.xiaoxun.apie.home_page.dialog.APieCreateGroupDialog
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

/**
 * 创建计划的半层页面
 */
class APieCreateFragment(
    private val repo: IIndexHomeRepo,
    private val viewModel: IndexHomeViewModel,
    private val planModelInfo: PlanModeInfo? = null
) : APieBaseBottomSheetDialogFragment<LayoutApieCreatePlanFragmentBinding>(
    LayoutApieCreatePlanFragmentBinding::inflate
) {

    private lateinit var frequencyAdapter: APiePlanFrequencyAdapter
    private lateinit var groupAdapter: APieGroupAdapter

    private val isReedit: Boolean by lazy { planModelInfo != null }

    private val loadingDialog: APieLoadingDialog by lazy { APieLoadingDialog(requireContext()) }

    private var selectedFrequency: PlanListType? = null
    private var selectedGroup: PlanGroupModel? = null

    companion object {
        private const val TAG = "APieCreateFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObserver()
        initView()
        initReeditData()
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
                Pair(it.planStartTime, DateTimeUtils.conversionTime(it.planStartTime, "yyyy-MM-dd"))
            )
            viewModel.updateSelectTimeRange(
                TimeRangeType.STOP_TIME,
                Pair(it.planStopTime, DateTimeUtils.conversionTime(it.planStopTime, "yyyy-MM-dd"))
            )
        }
    }

    private fun initView() {
        initFrequencyRecyclerView()
        initGroupRecyclerView()
        initBottomView()
        binding.nameEdit.setEditTextMaxInput(20)
        binding.createGroupLayout.setDebouncingClickListener {
            val dialog = APieCreateGroupDialog(
                titleRes = R.string.apie_create_plan_group_title,
                context = requireContext(),
                onConfirm = { name ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        repo.createPlanGroup(name)
                    }
                },
                onCancel = {}
            )
            dialog.show()
        }
        binding.cancelLayout.setDebouncingClickListener { dismiss() }
        binding.submitLayout.setDebouncingClickListener {
            if (needIntercept().not()) {
                createOrReeditPlan()
            }
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
        groupAdapter = APieGroupAdapter { pos, item ->
            viewModel.updateSelectPlanGroup(item.groupId)
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
            repo.loadPlanGroupList()
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
        viewModel.planGroupList.observe(viewLifecycleOwner) { newList ->
            // 更新数据
            val oldList = groupAdapter.getItems()
            if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                // 滚动到前面
                binding.planGroupRv.scrollToPosition(0)
            }
            groupAdapter.updateData(newList)
        }
        // 创建计划状态
        viewModel.createPlanState.observe(viewLifecycleOwner) { handleCreatePlanState(it) }
        // 计划类型选择状态
        viewModel.selectPlanType.observe(viewLifecycleOwner) { handleFrequencyChange(it) }
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
                loadingDialog.show()
            }

            CreatePlanState.SUCCESS -> {
                // 创建计划成功
                APieToast.showDialog("创建计划成功")
                loadingDialog.dismiss()
                dismiss()
            }

            CreatePlanState.FAILED -> {
                // 创建计划失败
                loadingDialog.dismiss()
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
            DateTimeUtils.conversionTime(planModelInfo?.planStopTime ?: 0, "yyyy-MM-dd")
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
            planStopTime = viewModel.getSelectStopTime() ?: 0,
            planTotalGoldCount = getPlanTotalGoldCount(),
            planObtainedGoldCount = 0
        )
    }

    // 计算这个任务最大的金币数量
    private fun getPlanTotalGoldCount(): Int {
        val awardCount = binding.awardCountEdit.text.toString().toIntOrNull() ?: 1
        val frequencyCount = binding.frequencyCountEdit.text.toString().toIntOrNull() ?: 1

        val startTime = viewModel.getSelectStartTime() ?: 0
        val stopTime = viewModel.getSelectStopTime() ?: 0

        val count = when (selectedFrequency) {
            PlanListType.SINGLE_PLAN -> awardCount
            PlanListType.TODAY_PLAN -> DateTimeUtils.calculateTimeDifference(startTime, stopTime, UnitType.DAY) * awardCount * frequencyCount
            PlanListType.WEEK_PLAN -> DateTimeUtils.calculateTimeDifference(startTime, stopTime, UnitType.WEEK) * awardCount * frequencyCount
            PlanListType.MONTH_PLAN -> DateTimeUtils.calculateTimeDifference(startTime, stopTime, UnitType.MONTH) * awardCount * frequencyCount
            PlanListType.YEAR_PLAN -> DateTimeUtils.calculateTimeDifference(startTime, stopTime, UnitType.YEAR) * awardCount * frequencyCount
            PlanListType.CUSTOM_PLAN, PlanListType.CYCLE_PLAN -> 0
            else -> 0
        }
        APieLog.d(TAG, "getPlanTotalGoldCount: $count")
        return count
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
                val showText = DateTimeUtils.conversionTime(millisecond, "yyyy-MM-dd")
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

    /**
     * 检查是否可以提交
     */
    private fun needIntercept(): Boolean {
        val name = binding.nameEdit.text.toString()
        if (name.isEmpty()) {
            APieToast.showDialog("计划名称不能为空")
            return true
        }
        if (viewModel.selectPlanTypeIsInit()) {
            APieToast.showDialog("请选择计划类型")
            return true
        }

        viewModel.selectPlanGroup.value ?: run {
            APieToast.showDialog("请选择计划分组")
            return true
        }

//        val frequency = binding.frequencyCountEdit.text.toString()
//        if (frequency.isEmpty() && viewModel.selectPlanType.value == PlanListType.CUSTOM_PLAN) {
//            APieToast.showDialog("计划频率不能为空")
//            return false
//        }
        val award = binding.awardCountEdit.text.toString()
        if (award.isEmpty()) {
            APieToast.showDialog("奖励派币数量不能为空")
            return true
        }
//        val deduct = binding.deductCountEdit.text.toString()
//        if (deduct.isEmpty()) {
//            APieToast.showDialog("扣除派币数量不能为空")
//            return false
//        }

        viewModel.selectTimeRange.value?.let {
            if ((it[TimeRangeType.START_TIME]?.first ?: 0) <= 0) {
                APieToast.showDialog("请选择开始时间")
                return true
            }
            if ((it[TimeRangeType.STOP_TIME]?.first ?: 0) <= 0) {
                APieToast.showDialog("请选择结束时间")
                return true
            }
        } ?: run {
            APieToast.showDialog("请选择时间范围")
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetCreatePlanStatus()
    }
}