package com.xiaoxun.apie.home_page.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.ui.APieStyleExitTip
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.home_page.adapter.setting.APieGroupManagerAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieGroupManagerSettingFragmentBinding
import com.xiaoxun.apie.home_page.dialog.APieCreateGroupDialog
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.repo.IndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.CreatePlanGroupState
import com.xiaoxun.apie.home_page.viewmodel.DeletePlanGroupState
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModelFactory
import com.xiaoxun.apie.home_page.viewmodel.LoadPlanGroupListState
import kotlinx.coroutines.launch

class APieGroupManagerFragment :
    APieBaseBottomSheetDialogFragment<LayoutApieGroupManagerSettingFragmentBinding>(
        LayoutApieGroupManagerSettingFragmentBinding::inflate
    ) {

    private val viewModel: IndexHomeViewModel by lazy {
        ViewModelProvider(context as APieBaseBindingActivity<*>, IndexHomeViewModelFactory()).get(
            IndexHomeViewModel::class.java
        )
    }

    private val repo: IIndexHomeRepo by lazy { IndexHomeRepo(viewModel) }

    private val groupAdapter: APieGroupManagerAdapter by lazy { APieGroupManagerAdapter() }

    private val loadingDialog: APieLoadingDialog by lazy {
        APieLoadingDialog(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableCancel = true
        super.onCreate(savedInstanceState)
        val screenHeight = UIUtils.getScreenRealHeight(requireContext())
        peekHeight = screenHeight / 2
        layoutHeight = screenHeight / 2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initObserver()
        initView()
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.loadPlanGroupList()
        }
    }

    private fun initObserver() {
        viewModel.createPlanGroupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                CreatePlanGroupState.START -> loadingDialog.show()
                CreatePlanGroupState.SUCCESS -> loadingDialog.dismiss()
                CreatePlanGroupState.FAILED -> loadingDialog.dismiss()
                else -> {}
            }
        }

        viewModel.deletePlanGroupState.observe(viewLifecycleOwner) { state ->
            when (state) {
                DeletePlanGroupState.START -> loadingDialog.show()
                DeletePlanGroupState.SUCCESS -> loadingDialog.dismiss()
                DeletePlanGroupState.FAILED -> loadingDialog.dismiss()
                else -> {}
            }
        }

        viewModel.loadPlanGroupListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                LoadPlanGroupListState.START -> loadGroupStart()
                LoadPlanGroupListState.SUCCESS -> loadGroupSuccess()
                LoadPlanGroupListState.FAILED -> loadGroupError()
                else -> {}
            }
        }
        viewModel.planGroupList.observe(viewLifecycleOwner) { newList ->
            // 更新数据
            val oldList = groupAdapter.getItems()
            if (oldList.isNotEmpty() && (newList.size > oldList.size)) {
                // 滚动到顶部
                binding.groupManagerRecyclerView.scrollToPosition(0)
            }
            groupAdapter.updateData(newList)
        }
    }

    private fun initView() {
        initRecyclerView()
        binding.addGroupIcon.setDebouncingClickListener {
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
    }

    private fun initRecyclerView() {
        binding.groupManagerCardView.layoutParams.apply {
            height = UIUtils.getScreenRealHeight(requireContext()) / 2 - 80.dp
        }
        binding.groupManagerRecyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context)
        }
        groupAdapter.setOnGroupManagerItemClickListener(object :
            APieGroupManagerAdapter.OnGroupManagerItemClickListener {
            override fun onGroupItemEditClick(position: Int, item: PlanGroupModel) {
                APieToast.showDialog("编辑分组${item.groupName}")
            }

            override fun onGroupItemDeleteClick(position: Int, item: PlanGroupModel) {
                APieStyleExitTip.show(
                    activity = requireActivity(),
                    APieStyleExitTip.APIE_DELETE_GROUP_KEY,
                    actionCallback = {
                        viewLifecycleOwner.lifecycleScope.launch {
                            repo.deleteGroup(item.groupId)
                        }
                    }
                )
            }
        })
    }

    private fun loadGroupStart() {
        binding.groupManagerRecyclerView.hide()
        binding.groupLoadingLayout.show()
        binding.groupLoadingView.show()
        binding.groupErrorTips.hide()
    }

    private fun loadGroupSuccess() {
        binding.groupManagerRecyclerView.show()
        binding.groupLoadingLayout.hide()
        binding.groupLoadingView.hide()
        binding.groupErrorTips.hide()
    }

    private fun loadGroupError() {
        binding.groupManagerRecyclerView.hide()
        binding.groupLoadingLayout.hide()
        binding.groupLoadingView.hide()
        binding.groupErrorTips.show()
    }
}