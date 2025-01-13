package com.xiaoxun.apie.home_page.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.ui.APieStyleExitTip
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.toast.APieToast
import com.xiaoxun.apie.common_model.home_page.group.PlanGroupModel
import com.xiaoxun.apie.home_page.adapter.setting.APieGroupManagerAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieGroupManagerSettingFragmentBinding
import com.xiaoxun.apie.home_page.dialog.APieCreateGroupDialog
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.repo.IndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModelFactory
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
        initRecyclerView()
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.loadPlanGroupList()
        }
    }

    private fun initObserver() {
        viewModel.planGroupList.observe(viewLifecycleOwner) { newList ->
            groupAdapter.replayData(newList)
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
        groupAdapter.setOnGroupManagerItemClickListener(object : APieGroupManagerAdapter.OnGroupManagerItemClickListener {
            override fun onGroupItemEditClick(position: Int, item: PlanGroupModel) {
                APieToast.showDialog("编辑分组${item.groupName}")
            }

            override fun onGroupItemDeleteClick(position: Int, item: PlanGroupModel) {
                APieStyleExitTip.show(
                    activity = requireActivity(),
                    APieStyleExitTip.APIE_DELETE_GROUP_KEY,
                    actionCallback = {
//                        viewLifecycleOwner.lifecycleScope.launch {
//                        }
                    }
                )
            }
        })
    }
}