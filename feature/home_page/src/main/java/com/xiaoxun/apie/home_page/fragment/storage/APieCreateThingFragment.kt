package com.xiaoxun.apie.home_page.fragment.storage

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.album.APieAlbumPickerHelper
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.config.APieConfig.THING_APPENDIX_MAX_COUNT
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadRoundCornerImage
import com.xiaoxun.apie.common.upload.APieUploadHelper
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.DateTimeUtils
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.storage.StatusType
import com.xiaoxun.apie.common_model.home_page.storage.StorageStatusModel
import com.xiaoxun.apie.common_model.home_page.thing.CreateThingInfo
import com.xiaoxun.apie.home_page.adapter.group.APieGroupAdapter
import com.xiaoxun.apie.home_page.adapter.thing.APieThingAppendixAdapter
import com.xiaoxun.apie.home_page.adapter.thing.APieThingStatusAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreateThingFragmentBinding
import com.xiaoxun.apie.home_page.dialog.APieCreateGroupDialog
import com.xiaoxun.apie.home_page.repo.thing.IThingRepo
import com.xiaoxun.apie.home_page.repo.thing.ThingGroupSource
import com.xiaoxun.apie.home_page.repo.thing.ThingRepoImpl
import com.xiaoxun.apie.home_page.viewmodel.AddImageSource
import com.xiaoxun.apie.home_page.viewmodel.CommonLoadingState
import com.xiaoxun.apie.home_page.viewmodel.CreateThingState
import com.xiaoxun.apie.home_page.viewmodel.IndexStorageViewModel
import com.xiaoxun.apie.home_page.viewmodel.LoadGroupListState
import com.xiaoxun.apie.home_page.viewmodel.TimeRangeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class APieCreateThingFragment(
    private val viewModel: IndexStorageViewModel
) :
    APieBaseBottomSheetDialogFragment<LayoutApieCreateThingFragmentBinding>(
        LayoutApieCreateThingFragmentBinding::inflate
    ) {
    companion object {
        private const val TAG = "APieCreateThingFragment"
    }

    private val loadingDialog: APieLoadingDialog by lazy {
        APieLoadingDialog(requireContext())
    }

    private val repo: IThingRepo by lazy {
        ThingRepoImpl(viewModel)
    }

    private val statusAdapter: APieThingStatusAdapter by lazy {
        APieThingStatusAdapter()
    }

    private lateinit var groupAdapter: APieGroupAdapter

    private lateinit var thingAppendixAdapter: APieThingAppendixAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        //enableCancel = true
        super.onCreate(savedInstanceState)
        //val screenHeight = UIUtils.getScreenRealHeight(requireContext())
        //peekHeight = screenHeight / 2
        APieAlbumPickerHelper.register(this, object : APieAlbumPickerHelper.MediaPickerCallback {
            override fun onMediaSelected(
                source: APieAlbumPickerHelper.MediaSource,
                uris: List<Uri>
            ) {
                val uri = uris.firstOrNull() ?: return
                APieLog.d(TAG, "${source.name} selected: $uri")
                val url = APieUploadHelper.uploadImage(requireActivity(), uri)
                if (viewModel.isAddThingIcon()) {
                    viewModel.updateThingIconUrl(url)
                } else {
                    viewModel.addThingAppendixUrl(url)
                }
            }

            override fun onCanceled(source: APieAlbumPickerHelper.MediaSource) {
                APieLog.d(TAG, "${source.name} selection canceled")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initObserver()
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repo.loadThingGroups(ThingGroupSource.CREATE_PAGE)
        }
    }

    private fun initView() {
        binding.topBar.title.text = getString(R.string.apie_create_thing_title)
        initGroupView()
        initThingIconView()
        initStatusView()
        initDateSelectView()
        initThingAppendixView()
        initButtonView()
    }

    private fun initDateSelectView() {
        binding.buyTime.text =  getCurrentTimeStr()
        binding.thingWarranty.text = "请选择"
        binding.buyTime.setDebouncingClickListener {
            showSelectDayView(TimeRangeType.START_TIME, viewModel.getSelectStartTime() ?: 0)
        }
        binding.thingWarranty.setDebouncingClickListener {
            showSelectDayView(TimeRangeType.STOP_TIME, viewModel.getSelectStopTime() ?: 0)
        }
    }

    private fun initGroupView() {
        groupAdapter = APieGroupAdapter(itemClick = { _, item ->
            viewModel.updateSelectThingGroup(item)
        })
        binding.thingGroupRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
            //addItemDecoration(SpaceItemDecoration())
        }
        binding.createGroupLayout.setDebouncingClickListener {
            val dialog = APieCreateGroupDialog(
                titleRes = R.string.apie_create_thing_group_title,
                context = requireContext(),
                onConfirm = { name ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        repo.createThingGroup(name)
                    }
                },
                onCancel = {}
            )
            dialog.show()
        }
    }

    private fun initThingIconView() {
        binding.thingIcon.setDebouncingClickListener {
            viewModel.updateCurrentAddImageSource(AddImageSource.THING_ICON)
            openPhotoPicker()
        }
    }

    private fun initStatusView() {
        val status = mutableListOf(
            StorageStatusModel(StatusType.USEING),
            StorageStatusModel(StatusType.RETIRED),
            StorageStatusModel(StatusType.DAMAGED),
            StorageStatusModel(StatusType.LOST),
            StorageStatusModel(StatusType.DUSTY),
            StorageStatusModel(StatusType.SOLD)
        )
        statusAdapter.updateData(status)
        binding.statusRecyclerView.apply {
            adapter = statusAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initThingAppendixView() {
        thingAppendixAdapter = APieThingAppendixAdapter(
            context = requireContext(),
            onAddClick = {
                if (viewModel.thingAppendixUrlSize() >= THING_APPENDIX_MAX_COUNT) {
                    APieToast.showDialog("最多只能添加${THING_APPENDIX_MAX_COUNT}张图片")
                    return@APieThingAppendixAdapter
                }
                viewModel.updateCurrentAddImageSource(AddImageSource.THING_APPENDIX)
                openPhotoPicker()
            },
            onItemClick = { position, url ->
                // 点击预览
            },
            onDeleteClick = { position, url ->
                viewModel.removeThingAppendixUrl(url)
            }
        )

        binding.thingAppendixRv.apply {
            adapter = thingAppendixAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initObserver() {
        viewModel.thingIconUrl.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            setThingIcon(it)
        }

        viewModel.loadThingGroupListState.observe(viewLifecycleOwner) { handleGroupLoadState(it) }

        // 物品分组列表
        viewModel.thingGroupList.observe(viewLifecycleOwner) { newList ->
            // 更新数据
            val oldList = groupAdapter.getItems()
            if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                // 滚动到前面
                binding.thingGroupRv.scrollToPosition(0)
            }
            groupAdapter.updateData(newList)
        }

        // 通用loading状态
        viewModel.commonLoadingState.observe(viewLifecycleOwner) { handleCommonLoadingState(it) }

        // 更新创建物品状态
        viewModel.createThingState.observe(viewLifecycleOwner) {
            when (it) {
                CreateThingState.START -> loadingDialog.show()
                CreateThingState.SUCCESS -> {
                    loadingDialog.dismiss()
                    dismiss()
                }
                CreateThingState.FAILED -> loadingDialog.dismiss()
                else -> {}
            }
        }

        viewModel.thingAppendixUrls.observe(viewLifecycleOwner) {
            thingAppendixAdapter.updateData(it)
        }
    }

    private fun handleCommonLoadingState(state: CommonLoadingState) {
        when (state) {
            CommonLoadingState.START -> loadingDialog.show()
            CommonLoadingState.SUCCESS -> loadingDialog.dismiss()
            CommonLoadingState.FAILED -> loadingDialog.dismiss()
        }
    }

    private fun initButtonView() {
        binding.cancelLayout.setDebouncingClickListener { dismiss() }
        binding.submitLayout.setDebouncingClickListener {
            if (needIntercept().not()) {
                createOrReeditPlan()
            }
        }
    }

    private fun createOrReeditPlan() {
        val createThingInfo = buildCreateThingInfo()
        viewLifecycleOwner.lifecycleScope.launch {
            repo.createThing(createThingInfo)
        }
    }

    private fun handleGroupLoadState(state: LoadGroupListState) {
        when (state) {
            LoadGroupListState.START -> loadThingGroupStart()
            LoadGroupListState.SUCCESS -> loadThingGroupSuccess()
            LoadGroupListState.FAILED -> loadThingGroupFailed()
        }
    }

    private fun loadThingGroupStart() {
        binding.thingGroupRv.visibility = View.GONE
        binding.groupLoadingLayout.visibility = View.VISIBLE
        binding.groupLoadingView.show()
    }

    private fun loadThingGroupSuccess() {
        binding.thingGroupRv.visibility = View.VISIBLE
        binding.groupLoadingLayout.visibility = View.GONE
        binding.groupLoadingView.hide()
    }

    private fun loadThingGroupFailed() {
        binding.thingGroupRv.visibility = View.GONE
        binding.groupLoadingLayout.visibility = View.GONE
        binding.groupErrorTips.visibility = View.VISIBLE
        binding.groupLoadingView.hide()
    }

    private fun openPhotoPicker() {
        APieAlbumPickerHelper.showPickerDialog(parentFragmentManager)
    }

    private fun setThingIcon(url: String) {
        binding.addIcon.hide()
        binding.thingIcon.loadRoundCornerImage(
            requireActivity(),
            url,
            12.dp
        )
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
                    binding.buyTime.text = showText
                } else if (timeRangeType == TimeRangeType.STOP_TIME) {
                    binding.thingWarranty.text = showText
                }
            }
            .setOnCancel("取消") {}
            .build().show()
    }

    private fun getCurrentTimeStr(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    /**
     * 检查是否可以提交
     */
    private fun needIntercept(): Boolean {
        viewModel.thingIconUrl.value ?: run {
            APieToast.showDialog("请添加物品图片")
            return true
        }

        val name = binding.nameEdit.text.toString()
        if (name.isEmpty()) {
            APieToast.showDialog("物品名称不能为空")
            return true
        }

        viewModel.selectThingGroup.value ?: run {
            APieToast.showDialog("请选择物品分组")
            return true
        }
        val status = statusAdapter.getSelectedStatus()?.statusType?.type?:0
        if (status == 0) {
            APieToast.showDialog("请选择物品状态")
            return true
        }

        val thingPrice = binding.thingPriceTitleEdit.text?.toString()
        if (thingPrice.isNullOrEmpty()) {
            APieToast.showDialog("物品价格不能为空")
            return true
        }

        viewModel.selectTimeRange.value?.let {
            if ((it[TimeRangeType.START_TIME]?.first ?: 0) <= 0) {
                APieToast.showDialog("请选择购买时间")
                return true
            }
            if ((it[TimeRangeType.STOP_TIME]?.first ?: 0) <= 0) {
                APieToast.showDialog("请选择保修期")
                return true
            }
        } ?: run {
            APieToast.showDialog("请选择时间范围")
            return true
        }
        return false
    }

    private fun buildCreateThingInfo(): CreateThingInfo {
        return CreateThingInfo(
            thingName = binding.nameEdit.text.toString(),
            belongGroupId = viewModel.selectThingGroup.value?.groupId ?: "",
            thingPrice = binding.thingPriceTitleEdit.text.toString().toDouble(),
            thingTags = mutableListOf(),
            thingIcon = viewModel.thingIconUrl.value ?: "",
            thingStatus = statusAdapter.getSelectedStatus()?.statusType?.type ?: 0,
            thingDesc = binding.thingDescEdit.text.toString(),
            buyAt = DateTimeUtils.timestampToDate(viewModel.getSelectStartTime() ?: 0),
            warrantyPeriod = DateTimeUtils.timestampToDate(viewModel.getSelectStopTime() ?: 0),
            thingAppendixList = viewModel.thingAppendixUrls.value?.toMutableList() ?: mutableListOf()
        )
    }
}