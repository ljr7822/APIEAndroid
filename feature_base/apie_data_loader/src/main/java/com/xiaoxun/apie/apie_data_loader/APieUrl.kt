package com.xiaoxun.apie.apie_data_loader

enum class APieUrl(val url: String) {
    ACCOUNT_LOGIN_PASSWORD(ACCOUNT_LOGIN_PASSWORD_URL),
    ACCOUNT_LOGIN_SMS_CODE(ACCOUNT_LOGIN_SMS_CODE_URL),
    ACCOUNT_SEND_SMS_CODE(ACCOUNT_SEND_SMS_CODE_URL),
    ACCOUNT_GET_STS_TOKEN(ACCOUNT_GET_STS_TOKEN_URL),
    ACCOUNT_GET_PUBLIC_KEY(ACCOUNT_GET_PUBLIC_KEY_URL),
    ACCOUNT_GET_USER_INFO(ACCOUNT_GET_USER_INFO_URL),
    ACCOUNT_CHANGE_PASSWORD(ACCOUNT_CHANGE_PASSWORD_URL),

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
    GET_DESIRE_GROUP_BY_USER_ID(GET_DESIRE_GROUP_BY_USER_ID_URL),
    EXCHANGE_DESIRE(EXCHANGE_DESIRE_URL),
    CREATE_DESIRE_GROUP(CREATE_DESIRE_GROUP_URL),

    GET_THING_GROUPS(GET_THING_GROUPS_URL),
    CREATE_THING_GROUP(CREATE_THING_GROUP_URL),
    GET_THING_LIST(GET_THING_LIST_URL),
    CREATE_THING(CREATE_THING_URL),
    DELETE_THING(DELETE_THING_URL),
}

internal const val ACCOUNT_LOGIN_PASSWORD_URL = "/user/loginByPassword"
internal const val ACCOUNT_LOGIN_SMS_CODE_URL = "/user/loginBySmsCode"
internal const val ACCOUNT_SEND_SMS_CODE_URL = "/sms/sendSMSCode/{phoneNum}/"
internal const val ACCOUNT_GET_STS_TOKEN_URL = "/permissions/getStsToken"
internal const val ACCOUNT_GET_PUBLIC_KEY_URL = "/permissions/getPublicKey"
internal const val ACCOUNT_GET_USER_INFO_URL = "/user/queryUser/{userId}"
internal const val ACCOUNT_CHANGE_PASSWORD_URL = "/user/changePassword"

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
internal const val GET_DESIRE_GROUP_BY_USER_ID_URL = "/desireGroup/queryGroup/{userId}"
internal const val CREATE_DESIRE_GROUP_URL = "/desireGroup/create"

internal const val GET_THING_GROUPS_URL = "/thingGroup/queryAllGroup/{userId}"
internal const val CREATE_THING_GROUP_URL = "/thingGroup/create"
internal const val GET_THING_LIST_URL = "/thing/queryAllThing/{userId}"
internal const val CREATE_THING_URL = "/thing/create"
internal const val DELETE_THING_URL = "/thing/deleteThing/{thingId}"
