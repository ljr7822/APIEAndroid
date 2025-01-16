package com.xiaoxun.apie.apie_data_loader

enum class APieUrl(val url: String) {
    ACCOUNT_LOGIN_PASSWORD(ACCOUNT_LOGIN_PASSWORD_URL),
    ACCOUNT_LOGIN_SMS_CODE(ACCOUNT_LOGIN_SMS_CODE_URL),
    ACCOUNT_SEND_SMS_CODE(ACCOUNT_SEND_SMS_CODE_URL),
    ACCOUNT_GET_ALL_PLAN_BY_USER_ID(ACCOUNT_GET_ALL_PLAN_BY_USER_ID_URL),
    GET_ALL_PLAN_GROUP_BY_USER_ID(GET_ALL_PLAN_GROUP_BY_USER_ID_URL),
    CREATE_PLAN(CREATE_PLAN_URL),
    CREATE_GROUP(CREATE_GROUP_URL),
    UPDATE_PLAN_COMPLETED_COUNT(UPDATE_PLAN_COMPLETED_COUNT_URL),
    DELETE_PLAN(DELETE_PLAN_URL),
    DELETE_GROUP(DELETE_GROUP_URL),
    UPDATE_GROUP(UPDATE_GROUP_URL),
    ACCOUNT_REGISTER(ACCOUNT_REGISTER_URL),
    CREATE_DESIRE(CREATE_DESIRE_URL),
    GET_DESIRE_BY_USER_ID(GET_DESIRE_BY_USER_ID_URL),
    EXCHANGE_DESIRE(EXCHANGE_DESIRE_URL),
}

internal const val ACCOUNT_LOGIN_PASSWORD_URL = "/user/loginByPassword"
internal const val ACCOUNT_LOGIN_SMS_CODE_URL = "/user/loginBySmsCode"
internal const val ACCOUNT_SEND_SMS_CODE_URL = "/sms/sendSMSCode/{phoneNum}/{userId}"
internal const val ACCOUNT_GET_ALL_PLAN_BY_USER_ID_URL = "/plan/queryAllPlan/{userId}"
internal const val GET_ALL_PLAN_GROUP_BY_USER_ID_URL = "/planGroup/queryAllGroup/{userId}"
internal const val CREATE_PLAN_URL = "/plan/create"
internal const val CREATE_GROUP_URL = "/planGroup/create"
internal const val UPDATE_PLAN_COMPLETED_COUNT_URL = "/plan/completedCount/{optType}/{planId}"
internal const val DELETE_PLAN_URL = "/plan/planInvisible/{planId}"
internal const val DELETE_GROUP_URL = "/planGroup/deleteGroup/{groupId}"
internal const val UPDATE_GROUP_URL = "/planGroup/updateGroup"
internal const val ACCOUNT_REGISTER_URL = "/user/loginByPassword"

internal const val CREATE_DESIRE_URL = "/desire/create"
internal const val GET_DESIRE_BY_USER_ID_URL = "/desire/queryAllDesire/{userId}"
internal const val EXCHANGE_DESIRE_URL = "/desire/exchangeDesire/{desireId}"
