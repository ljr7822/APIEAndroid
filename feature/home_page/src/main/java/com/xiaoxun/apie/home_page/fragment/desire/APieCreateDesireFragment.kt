package com.xiaoxun.apie.home_page.fragment.desire

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.xiaoxun.apie.apie_data_loader.request.desire.CreateDesireRequestBody
import com.xiaoxun.apie.apie_data_loader.request.plan.CreatePlanRequestBody
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.utils.DateTimeUtils
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.base.IBaseGroupModel
import com.xiaoxun.apie.home_page.adapter.SpaceItemDecoration
import com.xiaoxun.apie.home_page.adapter.group.APieGroupAdapter
import com.xiaoxun.apie.home_page.bean.DesireModeInfo
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreateDesireFragmentBinding
import com.xiaoxun.apie.home_page.dialog.APieCreateGroupDialog
import com.xiaoxun.apie.home_page.repo.desire.IIndexDesireRepo
import com.xiaoxun.apie.home_page.viewmodel.CreateDesireState
import com.xiaoxun.apie.home_page.viewmodel.CreatePlanState
import com.xiaoxun.apie.home_page.viewmodel.IndexDesireViewModel
import com.xiaoxun.apie.home_page.viewmodel.LoadGroupListState
import com.xiaoxun.apie.home_page.viewmodel.PlanListType
import com.xiaoxun.apie.home_page.viewmodel.TimeRangeType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 创建心愿页面
 */
class APieCreateDesireFragment(
    private val repo: IIndexDesireRepo,
    private val viewModel: IndexDesireViewModel,
    private val desireModelInfo: DesireModeInfo? = null
) :
    APieBaseBottomSheetDialogFragment<LayoutApieCreateDesireFragmentBinding>(
        LayoutApieCreateDesireFragmentBinding::inflate
    ) {

    private lateinit var groupAdapter: APieGroupAdapter

    private var selectedGroup: IBaseGroupModel? = null

    private val isReedit: Boolean by lazy { desireModelInfo != null }

    private val loadingDialog: APieLoadingDialog by lazy { APieLoadingDialog(requireContext()) }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        enableCancel = true
//        super.onCreate(savedInstanceState)
//        val screenHeight = UIUtils.getScreenRealHeight(requireContext())
//        peekHeight = screenHeight / 2
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObserver()
        initView()
        initReeditData()
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.loadDesireGroupList()
        }
        viewModel.updateSelectTimeRange(
            TimeRangeType.START_TIME,
            Pair(System.currentTimeMillis(), getCurrentTimeStr())
        )
    }

    private fun initObserver() {
        // 分组列表加载状态
        viewModel.loadDesireGroupListState.observe(viewLifecycleOwner) { handleGroupLoadState(it) }
        // 心愿分组列表
        viewModel.desireGroupList.observe(viewLifecycleOwner) { newList ->
            // 更新数据
            val oldList = groupAdapter.getItems()
            if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                // 滚动到前面
                binding.desireGroupRv.scrollToPosition(0)
            }
            groupAdapter.updateData(newList)
        }

        // 创建计划状态
        viewModel.createDesireState.observe(viewLifecycleOwner) { handleCreateDesireState(it) }
        // 更新时间区间
        viewModel.selectTimeRange.observe(viewLifecycleOwner) { handleSelectTimeRangeChange(it) }
    }

    private fun initView() {
        initGroupRecyclerView()
        initBottomView()

        binding.createGroupLayout.setDebouncingClickListener {
            val dialog = APieCreateGroupDialog(
                titleRes = R.string.apie_create_desire_group_title,
                context = requireContext(),
                onConfirm = { name ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        repo.createDesireGroup(name)
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
            showSelectDayView(
                TimeRangeType.STOP_TIME,
                viewModel.getSelectStopTime() ?: System.currentTimeMillis()
            )
        }

        binding.stopTime.text = "无限期"
    }

    private fun initGroupRecyclerView() {
        groupAdapter = APieGroupAdapter { pos, item ->
            viewModel.updateSelectDesireGroup(item.groupId)
            selectedGroup = item
        }

        binding.desireGroupRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
            //addItemDecoration(SpaceItemDecoration())
        }
    }

    private fun initReeditData() {

    }

    private fun handleGroupLoadState(state: LoadGroupListState) {
        when (state) {
            LoadGroupListState.START -> loadPlanGroupStart()
            LoadGroupListState.SUCCESS -> loadPlanGroupSuccess()
            LoadGroupListState.FAILED -> loadPlanGroupFailed()
        }
    }

    private fun loadPlanGroupStart() {
        binding.desireGroupRv.visibility = View.GONE
        binding.groupLoadingLayout.visibility = View.VISIBLE
        binding.groupLoadingView.show()
    }

    private fun loadPlanGroupSuccess() {
        binding.desireGroupRv.visibility = View.VISIBLE
        binding.groupLoadingLayout.visibility = View.GONE
        binding.groupLoadingView.hide()
    }

    private fun loadPlanGroupFailed() {
        binding.desireGroupRv.visibility = View.GONE
        binding.groupLoadingLayout.visibility = View.GONE
        binding.groupErrorTips.visibility = View.VISIBLE
        binding.groupLoadingView.hide()
    }

    private fun initBottomView() {
        if (isReedit) {
            binding.topBar.title.setText(R.string.apie_create_desire_reedit_title)
            binding.cancelBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_reedit_cancel_btn_tip)
            binding.submitBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_reedit_submit_btn_tip)
        } else {
            binding.topBar.title.setText(R.string.apie_create_desire_title)
            binding.cancelBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_cancel_btn_tip)
            binding.submitBtnTip.setText(com.xiaoxun.apie.common.R.string.apie_create_plan_submit_btn_tip)
        }
    }


    private fun getCurrentTimeStr(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun showSelectDayView(timeRangeType: TimeRangeType, defaultTime: Long) {
        CardDatePickerDialog.builder(requireContext())
            .setTitle(timeRangeType.desc)
            .setDisplayType(mutableListOf(0, 1, 2))
            .setDefaultTime(defaultTime)
            .setBackGroundModel(2)
            .showBackNow(false)
            .setWrapSelectorWheel(false)
            .setThemeColor(requireContext().getColor(R.color.apie_color_6F94F4))
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

    private fun handleCreateDesireState(state: CreateDesireState) {
        when (state) {
            CreateDesireState.START -> {
                // 开始创建计划
                loadingDialog.show()
            }

            CreateDesireState.SUCCESS -> {
                // 创建计划成功
                APieToast.showDialog("创建心愿成功")
                loadingDialog.dismiss()
                dismiss()
            }

            CreateDesireState.FAILED -> {
                // 创建计划失败
                loadingDialog.dismiss()
            }

            else -> {}
        }
    }

    /**
     * 选择时间范围变化后，调整开始和结束日期
     */
    private fun handleSelectTimeRangeChange(rangeMap: HashMap<TimeRangeType, Pair<Long, String>>) {
        binding.startTime.text = rangeMap[TimeRangeType.START_TIME]?.second ?: getCurrentTimeStr()
        binding.stopTime.text = rangeMap[TimeRangeType.STOP_TIME]?.second ?: "请选择"
    }

    private fun needIntercept(): Boolean {
        val name = binding.nameEdit.text.toString()
        if (name.isEmpty()) {
            APieToast.showDialog("心愿名称不能为空")
            return true
        }

        viewModel.selectDesireGroup.value ?: run {
            APieToast.showDialog("请选择心愿分组")
            return true
        }

        val award = binding.awardCount.getNumber()
        if (award <= 0) {
            APieToast.showDialog("需要输入兑换金币")
            return true
        }

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

    private fun createOrReeditPlan() {
        if (isReedit) {
            APieToast.showDialog("编辑心愿")
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                repo.createDesire(buildCreateDesireRequestBody())
            }
        }
    }

    private fun buildCreateDesireRequestBody(): CreateDesireRequestBody {
        return CreateDesireRequestBody(
            desireName = binding.nameEdit.text.toString(),
            desireIcon = "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
            desireBelongGroupId = selectedGroup?.groupId ?: "",
            desirePrice = binding.awardCount.getNumber(),
            desireType = 1,
            desireWeight = 10,
            createUserId = AccountManager.getUserId(),
            desireTotalCount = 10,
            desireExchangeCount = 0
        )
    }
}