package com.xiaoxun.apie.home_page.viewmodel

enum class LoadPlanListState {
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
 */
enum class PlanListType(val type: Int) {
    ALL_PLAN(0),    // 所有计划
    SINGLE_PLAN(1), // 单次计划
    TODAY_PLAN(2),  // 今日计划
    WEEK_PLAN(3),   // 本周计划
    MONTH_PLAN(4),  // 本月计划
    YEAR_PLAN(5),   // 本年计划
    CUSTOM_PLAN(6), // 自定义计划
    DONE_PLAN(7),   // 已完成计划
    DOING_PLAN(8)   // 进行中计划
}

enum class FilterStatus(val status: Int) {
    ALL(0),     // 全部
    DONE(1),    // 已完成
    DOING(2)    // 进行中
}