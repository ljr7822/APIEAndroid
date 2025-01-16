package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lxj.xpopup.XPopup
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.ui.APieLoadingDialog
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.desire.DesireModel
import com.xiaoxun.apie.gold_manage.service.GoldService
import com.xiaoxun.apie.home_page.adapter.desire.APieDesireAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexDesireFragmentBinding
import com.xiaoxun.apie.home_page.repo.desire.IIndexDesireRepo
import com.xiaoxun.apie.home_page.repo.desire.IndexDesireRepoImpl
import com.xiaoxun.apie.home_page.viewmodel.GenericViewModelFactory
import com.xiaoxun.apie.home_page.viewmodel.IndexDesireViewModel
import com.xiaoxun.apie.home_page.viewmodel.LoadDesireListState
import com.xiaoxun.apie.home_page.widget.APieLeftDrawerPopupView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * 心愿Fragment
 */
class APieIndexDesireFragment : APieBaseBindingFragment<LayoutApieIndexDesireFragmentBinding>(
    LayoutApieIndexDesireFragmentBinding::inflate
) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    private val viewModel: IndexDesireViewModel by lazy {
        ViewModelProvider(
            hostActivity,
            GenericViewModelFactory { IndexDesireViewModel() })[IndexDesireViewModel::class.java]
    }

    private val goldService: GoldService by lazy { GoldService() }

    private val repo: IIndexDesireRepo by lazy { IndexDesireRepoImpl(viewModel) }

    private val desireAdapter: APieDesireAdapter by lazy { APieDesireAdapter() }

    private val loadingDialog: APieLoadingDialog by lazy { APieLoadingDialog(hostActivity) }

    private var refreshLayout: RefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGoldManager()
        initRecyclerView()
        initData()
        initObserver()
    }

    private fun initGoldManager() {
        goldService.initializeGoldManager()
        goldService.goldLiveData.observe(viewLifecycleOwner) {
            binding.expandView.setGoldValue(it.toString())
        }
    }

    private fun initRecyclerView() {
        binding.refreshLayout.apply {
            val header = MaterialHeader(activity).apply {
                setProgressBackgroundColorSchemeResource(com.xiaoxun.apie.common.R.color.apie_color_white)
                setColorSchemeResources(com.xiaoxun.apie.common.R.color.apie_color_6F94F4)
            }
            setRefreshHeader(header)
            setOnRefreshListener { refresh ->
                lifecycleScope.launch {
                    repo.loadDesireList(AccountMMKVRepository.userId, true)
                }
                refreshLayout = refresh
            }
        }
        binding.desireRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = desireAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            desireAdapter.hideDesireMenuLayer()
                        }
                    }
                }
            })
        }
        desireAdapter.setItemClickListener(object : APieDesireAdapter.ItemClickListener {
            override fun onItemBuyClick(position: Int, desireModel: DesireModel) {
                lifecycleScope.launch {
                    // TODO:购买
                }
            }

            override fun onItemDataAnalysisClick(position: Int, desireModel: DesireModel) {
                // 数据分析
            }

            override fun onItemResetClick(position: Int, desireModel: DesireModel) {
                // 退回
            }

            override fun onItemEditClick(position: Int, desireModel: DesireModel) {
                // 编辑
            }

            override fun onItemDeleteClick(position: Int, desireModel: DesireModel) {
                // 删除
            }
        })
    }

    private fun initData() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repo.loadDesireList(AccountMMKVRepository.userId)
        }
    }

    private fun initObserver() {
        viewModel.loadDesireListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                LoadDesireListState.START -> loadingDialog.show()
                LoadDesireListState.SUCCESS -> {
                    loadingDialog.dismiss()
                    refreshLayout?.finishRefresh(100)
                }
                LoadDesireListState.FAILED -> {
                    loadingDialog.dismiss()
                    refreshLayout?.finishRefresh(100)
                }
                else -> {}
            }
        }

        viewModel.currentDesireList.observe(viewLifecycleOwner) {
            desireAdapter.updateData(it)
        }
    }

    override fun initTopBarView() {
        super.initTopBarView()
        binding.topBar.leftIconView.setDebouncingClickListener {
            showLeftDrawerPopupView()
        }
    }

    private fun showLeftDrawerPopupView() {
        XPopup.Builder(hostActivity)
            .isDestroyOnDismiss(true)
            .isViewMode(true)
            .asCustom(APieLeftDrawerPopupView(hostActivity))
            .show()
    }
}