package com.xiaoxun.apie.home_page.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.lxj.xpopup.impl.PartShadowPopupView
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.viewmodel.FilterStatus
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModel
import com.xiaoxun.apie.home_page.viewmodel.IndexHomeViewModelFactory
import com.xiaoxun.apie.home_page.viewmodel.PlanListType

class APieFilterPartShadowPopupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val gravity: Int
) : PartShadowPopupView(context) {

    private val viewModel: IndexHomeViewModel by lazy {
        ViewModelProvider(context as APieBaseBindingActivity<*>, IndexHomeViewModelFactory()).get(
            IndexHomeViewModel::class.java
        )
    }

    // 状态过滤
    private val statusFilterButtons: Map<FilterStatus, TextView> by lazy {
        mapOf(
            FilterStatus.ALL to findViewById(R.id.allPlan),
            FilterStatus.DONE to findViewById(R.id.donePlan),
            FilterStatus.DOING to findViewById(R.id.doingPlan)
        )
    }

    // 类型过滤
    private val planTypeButtons: Map<PlanListType, TextView> by lazy {
        mapOf(
            PlanListType.SINGLE_PLAN to findViewById(R.id.singlePlan),
            PlanListType.TODAY_PLAN to findViewById(R.id.todayPlan),
            PlanListType.WEEK_PLAN to findViewById(R.id.weekPlan),
            PlanListType.MONTH_PLAN to findViewById(R.id.monthPlan)
        )
    }

    private val resetBtn: View by lazy { findViewById(R.id.resetBtn) }
    private val closeBtn: View by lazy { findViewById(R.id.closeBtn) }

    override fun getImplLayoutId(): Int = R.layout.apie_filter_part_shadow_popup_view

    override fun onCreate() {
        super.onCreate()
        val params = findViewById<View>(R.id.rootView).layoutParams as LayoutParams
        params.gravity = gravity

        initView()
        initObserver()
    }

    private fun initView() {
        statusFilterButtons.forEach { (status, button) ->
            button.setDebouncingClickListener {
                viewModel.filterStatus.value = status
            }
        }

        planTypeButtons.forEach { (type, button) ->
            button.setDebouncingClickListener {
                viewModel.filterPlanType.value = type
            }
        }

        resetBtn.setDebouncingClickListener {
            viewModel.filterStatus.value = FilterStatus.ALL
            viewModel.filterPlanType.value = PlanListType.ALL_PLAN
        }

        closeBtn.setDebouncingClickListener {
            dismiss()
        }
    }

    private fun initObserver() {
        viewModel.filterStatus.observe(context as LifecycleOwner) { status ->
            statusFilterButtons.forEach { (key, button) ->
                val isSelected = key == status
                button.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        if (isSelected) R.color.apie_color_4770DC else R.color.apieTheme_colorBlack,
                        null
                    )
                )
                button.background = ResourcesCompat.getDrawable(
                    context.resources,
                    if (isSelected) R.drawable.apie_filter_plan_selected_bg else R.drawable.apie_filter_plan_normal_bg,
                    null
                )
            }
        }

        viewModel.filterPlanType.observe(context as LifecycleOwner) { type ->
            planTypeButtons.forEach { (key, button) ->
                val isSelected = key == type
                button.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        if (isSelected) R.color.apie_color_4770DC else R.color.apieTheme_colorBlack,
                        null
                    )
                )
                button.background = ResourcesCompat.getDrawable(
                    context.resources,
                    if (isSelected) R.drawable.apie_filter_plan_selected_bg else R.drawable.apie_filter_plan_normal_bg,
                    null
                )
            }
        }
    }
}