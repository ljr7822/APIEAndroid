package com.xiaoxun.apie.common.utils.account

import com.xiaoxun.apie.common.utils.SharedPreferencesHelper
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_NAME_KEY
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_PHONE_KEY
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_TOKEN_KEY
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_USERID_KEY

object AccountManager {

    fun isLogin(): Boolean {
//        return getUserId().isNotEmpty() && getToken().isNotEmpty() && getPhoneNum().isNotEmpty()
        return false
    }

    fun getUserId(): String {
        return SharedPreferencesHelper.getString(SP_ACCOUNT_USERID_KEY)
    }

    fun getToken(): String {
        return SharedPreferencesHelper.getString(SP_ACCOUNT_TOKEN_KEY)
    }

    fun getPhoneNum(): String {
        return SharedPreferencesHelper.getString(SP_ACCOUNT_PHONE_KEY)
    }

    fun getUserName(): String {
        return SharedPreferencesHelper.getString(SP_ACCOUNT_NAME_KEY)
    }

}