package com.xiaoxun.apie.home_page.fragment.storage

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.album.APieAlbumPickerHelper
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadRoundCornerImage
import com.xiaoxun.apie.common.upload.APieUploadHelper
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.storage.StatusType
import com.xiaoxun.apie.common_model.home_page.storage.StorageStatusModel
import com.xiaoxun.apie.home_page.adapter.SpaceItemDecoration
import com.xiaoxun.apie.home_page.adapter.group.APieGroupAdapter
import com.xiaoxun.apie.home_page.adapter.storage.StorageGroupAdapter
import com.xiaoxun.apie.home_page.adapter.storage.StorageStatusAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreateThingFragmentBinding
import com.xiaoxun.apie.home_page.dialog.APieCreateGroupDialog
import com.xiaoxun.apie.home_page.repo.thing.IThingRepo
import com.xiaoxun.apie.home_page.repo.thing.ThingGroupSource
import com.xiaoxun.apie.home_page.repo.thing.ThingRepoImpl
import com.xiaoxun.apie.home_page.viewmodel.CommonLoadingState
import com.xiaoxun.apie.home_page.viewmodel.IndexStorageViewModel
import com.xiaoxun.apie.home_page.viewmodel.LoadGroupListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class APieCreateThingFragment(
    private val viewModel: IndexStorageViewModel
) :
    APieBaseBottomSheetDialogFragment<LayoutApieCreateThingFragmentBinding>(
        LayoutApieCreateThingFragmentBinding::inflate
    ) {
    companion object {
        private const val TAG = "APieCreateThingFragment"
    }

    private val loadingDialog: APieLoadingDialog by lazy { APieLoadingDialog(requireContext()) }

    private val repo: IThingRepo by lazy {
        ThingRepoImpl(viewModel)
    }

    private val statusAdapter: StorageStatusAdapter by lazy {
        StorageStatusAdapter()
    }

    private lateinit var groupAdapter: APieGroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APieAlbumPickerHelper.register(this, object : APieAlbumPickerHelper.MediaPickerCallback {
            override fun onMediaSelected(
                source: APieAlbumPickerHelper.MediaSource,
                uris: List<Uri>
            ) {
                val uri = uris.firstOrNull() ?: return
                APieLog.d(TAG, "${source.name} selected: $uri")

                val url = APieUploadHelper.uploadImage(requireActivity(), uri)
                viewModel.updateThingIconUrl(url)
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
        initGroupView()
        initThingIconView()
        initStatusView()
        initButtonView()
    }

    private fun initGroupView() {
        groupAdapter = APieGroupAdapter()
        binding.thingGroupRv.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
            addItemDecoration(SpaceItemDecoration())
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
//            if (needIntercept().not()) {
//                createOrReeditPlan()
//            }
            dismiss()
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
}