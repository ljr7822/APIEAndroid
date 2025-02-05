package com.xiaoxun.apie.home_page.bean



data class DesireModeInfo(
    // 心愿id
    val desireId: String = "",
    // 心愿名称
    val desireName: String = "",
    // 心愿图标
    val desireIcon: String = "",
    // 心愿所在分组id
    val desireBelongGroupId: String = "",
    // 心愿状态：1可见，0不可见
    val visibleStatus: Int = 1,
    // 心愿价格
    val desirePrice: Int,
    // 心愿类型
    val desireType: Int = 1,
    // 心愿权重
    val desireWeight: Int = 0,
    // 创建者id
    val createUserId: String = "",
    // 心愿总数
    val desireTotalCount: Int,
    // 心愿兑换数
    val desireExchangeCount: Int = 0,
)

fun DesireModeInfo.desireModel2DesireModeInfo(): DesireModeInfo {
    return DesireModeInfo(
        desireId = this.desireId,
        desireName = this.desireName,
        desireIcon = this.desireIcon,
        desireBelongGroupId = this.desireBelongGroupId,
        visibleStatus = this.visibleStatus,
        desirePrice = this.desirePrice,
        desireType = this.desireType,
        desireWeight = this.desireWeight,
        createUserId = this.createUserId,
        desireTotalCount = this.desireTotalCount,
        desireExchangeCount = this.desireExchangeCount
    )
}