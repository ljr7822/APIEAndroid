package com.xiaoxun.apie.home_page.bean

import com.xiaoxun.apie.common_model.home_page.plan.PlanModel
import com.xiaoxun.apie.home_page.viewmodel.PlanListType

data class PlanModeInfo(
    val planId: String = "",
    val planName: String = "",
    val planIcon: String = "",
    val planType: Int = 1,
    val belongGroupId: String = "",
    val planFrequency: Int = 1,
    val planAward: Int = 0,
    val planPunish: Int = 0,
    val planWeight: Int = 0,
    val createUserId: String = "",
    val planStartTime: Long = 0,
    val planStopTime: Long = 0
) {
    fun getPlanListTypeByPlanType(): PlanListType {
        return when (planType) {
            0 -> PlanListType.ALL_PLAN
            1 -> PlanListType.SINGLE_PLAN
            2 -> PlanListType.TODAY_PLAN
            3 -> PlanListType.WEEK_PLAN
            4 -> PlanListType.MONTH_PLAN
            5 -> PlanListType.YEAR_PLAN
            6 -> PlanListType.CUSTOM_PLAN
            7 -> PlanListType.CYCLE_PLAN
            else -> PlanListType.INIT_TYPE
        }
    }
}

fun PlanModel.planModel2PlanModeInfo(): PlanModeInfo {
    return PlanModeInfo(
        planId = this.planId,
        planName = this.planName,
        planIcon = this.planIcon,
        planType = this.planType,
        belongGroupId = this.belongGroupId,
        planFrequency = this.planFrequency,
        planAward = this.planAward,
        planPunish = this.planPunish,
        planWeight = this.planWeight,
        createUserId = this.createUserId,
        planStartTime = this.planStartTime,
        planStopTime = this.planStopTime
    )
}