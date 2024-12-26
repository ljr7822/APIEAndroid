package com.xiaoxun.apie.common.utils


/**
 * Created by susion on 17/8/28.
 */

object LoginRequestParam {
    const val ZONE = "zone"
    const val PHONE_NUMBER = "phone"
    const val MOBILE_TOKEN = "mobile_token"
    const val TOKEN = "token"
    const val GW_AUTH = "gw_auth"
    const val TYPE = "type"
    const val PHONE_PASSWORD = "password"
    const val UNBIND_OTHER_ACCOUNT = "unbind_other_account"
    const val VALIDATE_PHONE_TOKEN = "validate_phone_token"
    const val STATE_TOKEN = "state_token"
    const val FACE_TOKEN = "face_token"

    //秒验
    const val OP_TOKEN = "op_token"
    const val OPERATOR = "operator"

    //隐藏登录页返回按钮
    const val HIDE_BACK = "hide_back"
}


enum class ThirdPartyCommonLoginParam {
    type, openid, token, code
}

/**
 * 不同的注册类型
 */
object LoginType {
    const val LOGIN_TYPE = "loginType"// qq、weixin、weibo
    const val LOGIN_ACTION_TYPE = "type" // 前置登录or后置登录触发类型

    //登录方式 loginType
    const val LOGON_PHONE = "logon_phone"
    const val SCAN_LOGIN = "login_scan"
    const val LOGON_PHONE_PASSWORD = "logon_phone_password"
    const val LOGON_QUICK_LOGIN = "logon_quick_login" //一键登录
    const val LOGIN_SOURCE = "login_source"

    val PHONE = "phone"

    //忘记密码
    const val RESET_PASSWORD = "reset_password"
}

/**
 * 跳转界面的请求码
 */
object LoginRequestCode {
    const val SELECT_COUNTRY_PHONE_CODE = 100
    const val REQUEST_PERMISSION_CODE = 122
    const val REQUEST_PERMISSION_CAMERA = 123
    const val CAMERA_REQUEST_CODE = 202
}

/**
 * 手机区号
 */
object RegisterCountryPhoneCode {
    const val CHINA_PHONE_CODE = "86"
    const val AMERICA_PHONE_CODE = "1"
    const val SECRET_PHONE_CODE = "236"
    const val CHINA_PHONE_NUMBER_COUNT = 13  //算空格
    const val AMERICA_PHONE_NUMBER_COUNT = 12 //算空格
    const val MAX_COUNT = 20
}

/**
 * 导入头像的类型
 */
object RegisterImportAvatarType {
    const val WECHAT = 1002
    const val LOCAL = 1004
    const val LOCAL_CAMERA = 1005
    const val WECHAT_STR = "weixin"
}

/**
 * 性别
 */
object Gender {
    const val FEMALE = 1
    const val MALE = 0
    const val UNKNOW = 2
}

object VerifyCodeType {
    const val REGISTER = "register" //注册
    const val BIND = "phonebind" //绑定手机
    const val RESET_PASSWORD = "resetpwd" //重置密码
    const val RECOVER_PASSWORD = "recovery_pwd" //未登录找回-重设密码
    const val LOGON = "login" //登录
    const val VALIDATE_BIND = "validate_bind" //手机号换绑
    const val ACCOUNT_RECOVERY_BIND = "account_recovery_bind" //账号找回绑定手机号
    const val NOT_SUPPORT = "not_support"
}

object RegisterStepName {
    const val EXTRA_INFO_VIEW = "EXTRA_INFO_VIEW"
    const val FRIEND_IN_XHS_VIEW = "FRIEND_IN_XHS_VIEW"
    const val SELECT_INTEREST_TAG_VIEW = "SELECT_INTEREST_TAG_VIEW"
    const val FIND_USER_VIEW = "FIND_USER_VIEW"
    const val GENDER_SELECT_PAGE = "GENDER_SELECT_PAGE" //性别选择页
    const val BIRTH_SELECT_PAGE = "BIRTH_SELECT_PAGE" //年龄选择页
    const val POLYMERIZE_PAGE = "POLYMERIZE_PAGE" //聚合选择页
    const val SIMPLE_POLYMERIZE_PAGE = "SIMPLE_POLYMERIZE_PAGE" //简化版聚合选择页
}

/**
 * "corpId" : "xxxxxxx"  //企业号唯一ID
 * "corpNickname"："伊利"  //企业号昵称
 * "corpAvatar": "xxxxxxx 1/企业号头像
 * "corpName"："伊利集团"  1/企业名称
 * "'auxiliaryAccountId"："xxxxxxx"，//辅助账号uid
 * "operationType": "CREATE", //CREATE: QUE, BIND: 1$33
 * "token":
 */
object EnterpriseInviteParam {
    const val CORP_ID = "corpId"
    const val CORP_NICK_NAME = "corpNickname"
    const val CORP_AVATAR = "corpAvatar"
    const val CORP_NAME = "corpName"
    const val AUXILIARY_ACCOUNT_ID = "auxiliaryAccountId"
    const val OPERATION_TYPE = "operationType"
    const val TOKEN = "token"
}