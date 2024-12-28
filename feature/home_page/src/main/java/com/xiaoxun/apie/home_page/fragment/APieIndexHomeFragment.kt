package com.xiaoxun.apie.home_page.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.xiaoxun.apie.common.HOME_SETTING_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.base.fragment.APieBaseBindingFragment
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.data_loader.utils.ThreadUtils
import com.xiaoxun.apie.home_page.R
import com.xiaoxun.apie.home_page.activity.APieIndexActivity
import com.xiaoxun.apie.home_page.adapter.APiePlanAdapter
import com.xiaoxun.apie.home_page.adapter.SpaceItemDecoration
import com.xiaoxun.apie.home_page.databinding.LayoutApieIndexHomeFragmentBinding
import com.xiaoxun.apie.home_page.repo.IIndexHomeRepo
import com.xiaoxun.apie.home_page.repo.IndexHomeRepo
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class APieIndexHomeFragment: APieBaseBindingFragment<LayoutApieIndexHomeFragmentBinding>(LayoutApieIndexHomeFragmentBinding::inflate) {
    companion object {
        const val TEST_FLAG: String = "test_flag"
    }

    private val viewModel: IndexHomeViewModel by lazy {
        ViewModelProvider(hostActivity, IndexHomeViewModelFactory()).get(
            IndexHomeViewModel::class.java
        )
    }

    private val repo: IIndexHomeRepo by lazy { IndexHomeRepo(viewModel) }

    private val adapter: APiePlanAdapter by lazy { APiePlanAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initData()
        initObserver()
    }

    override fun initTopBarView() {
        super.initTopBarView()
        binding.topBar.leftIconView.setDebouncingClickListener {
            ARouter.getInstance().build(HOME_SETTING_ACTIVITY_PATH).navigation()
        }
    }

    private fun initData() {
        GlobalScope.launch(Dispatchers.Main) {
            repo.loadPlanList()
        }
    }

    private fun initObserver() {
        viewModel.planList.observe(viewLifecycleOwner) {
            // 更新数据
            adapter.replayData(it)
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
                    }
                }
            }
        })
    }
}