package com.xiaoxun.apie.home_page.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.lxj.xpopup.core.DrawerPopupView
import com.xiaoxun.apie.common.HOME_SETTING_ACTIVITY_PATH
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.account.AccountManager

class APieLeftDrawerPopupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawerPopupView(context) {

    private val timeTip: TextView by lazy { findViewById(R.id.timeTip) }
    private val userName: TextView by lazy { findViewById(R.id.userName) }
    private val scanIcon: LinearLayout by lazy { findViewById(R.id.scanIcon) }
    private val settingIcon: LinearLayout by lazy { findViewById(R.id.settingIcon) }

    override fun getImplLayoutId(): Int {
        return R.layout.apie_left_drawer_popup_view
    }

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {
        timeTip.text = getGreetingMessage()
        userName.text = AccountManager.getUserName()
        scanIcon.setOnClickListener {
            dismiss()
        }
        settingIcon.setOnClickListener {
            ARouter.getInstance().build(HOME_SETTING_ACTIVITY_PATH).navigation()
            dismiss()
        }
    }

    private fun getGreetingMessage(): String {
        val currentHour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return when (currentHour) {
            in 5..8 -> "早上好，" // 5:00-8:59
            in 9..11 -> "上午好，" // 9:00-11:59
            in 12..13 -> "中午好，" // 12:00-13:59
            in 14..17 -> "下午好，" // 14:00-17:59
            else -> "晚上好，" // 18:00-4:59
        }
    }

}