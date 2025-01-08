package com.xiaoxun.apie.home_page.viewmodel

enum class LoadPlanListState {
    START,
    SUCCESS,
    FAILED
}

enum class LoadPlanGroupListState {
    START,
    SUCCESS,
    FAILED
}

enum class CreatePlanState {
    INIT_STATUS,
    START,
    SUCCESS,
    FAILED
}

/**
 * 1、单次计划，即只有一次的计划(次数(planFrequency)=1)
 * 2、每日计划，即每天都要完成的计划(次数(planFrequency)可自定义)
 * 3、每周计划，即每周都要完成的计划(次数(planFrequency)可自定义)
 * 4、每月计划，即每月都要完成的计划(次数(planFrequency)可自定义)
 * 5、每年计划，即每年都要完成的计划(次数(planFrequency)可自定义)
 * 6、自定义计划(定目标)，即自定义时间段内都要完成的计划(次数(planFrequency)可自定义)
 * 7、无限(定目标)，即无限时间段内都要完成的计划(次数(planFrequency)可自定义)
 */
enum class PlanListType(val type: Int, val desc: String = "") {
    INIT_TYPE(-1, "初始化"), // 初始化
    ALL_PLAN(0, "全部"),      // 所有计划
    SINGLE_PLAN(1, "单次"),   // 单次计划
    TODAY_PLAN(2, "每天"),    // 今日计划
    WEEK_PLAN(3, "每周"),     // 本周计划
    MONTH_PLAN(4, "每月"),    // 本月计划
    YEAR_PLAN(5, "每年"),     // 本年计划
    CUSTOM_PLAN(6, "自定义"), // 自定义计划
    CYCLE_PLAN(7, "无限"),    // 无限计划
}

/**
 * 计划进行状态(planStatus)：0: 未开始，1: 进行中，2: 已完成，3: 已放弃
 */
enum class PlanStatus(val status: Int) {
    NOT_START(0),  // 未开始
    DONE(1),       // 已完成
    DOING(2),      // 进行中
    GIVE_UP(3)     // 已放弃
}

/**
 * 计划完成次数跟新类型
 */
enum class CompletedCountOptType(val type: Int) {
    INCREMENT(1), // 增加
    DECREMENT(0)  // 减少
}

/**
 * 时间区间选择类型-标题
 */
enum class TimeRangeType(val type: Int, val desc: String) {
    START_TIME(0, "选择开始时间"),
    STOP_TIME(1, "选择结束时间")
}