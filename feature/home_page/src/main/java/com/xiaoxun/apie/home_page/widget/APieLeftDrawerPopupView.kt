package com.xiaoxun.apie.home_page.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.lxj.xpopup.core.DrawerPopupView
import com.xiaoxun.apie.common.HOME_SETTING_ACTIVITY_PATH
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.manager.account.AccountManager
import com.xiaoxun.apie.common.repo.AccountMMKVRepository
import com.xiaoxun.apie.common.repo.DesireMMKVRepository
import com.xiaoxun.apie.common.repo.PlanMMKVRepository
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.home_page.fragment.setting.APieGroupManagerFragment
import com.xiaoxun.apie.home_page.fragment.setting.APieSoundEffectsFragment

class APieLeftDrawerPopupView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DrawerPopupView(context) {

    companion object {
        private const val TAG = "APieLeftDrawerPopupView"
    }

    private val timeTip: TextView by lazy { findViewById(R.id.timeTip) }
    private val userName: TextView by lazy { findViewById(R.id.userName) }
    private val scanIcon: LinearLayout by lazy { findViewById(R.id.scanIcon) }
    private val settingIcon: LinearLayout by lazy { findViewById(R.id.settingIcon) }

    private val totalPlanNum: TextView by lazy { findViewById(R.id.totalPlanNum) }
    private val totalDesireNum: TextView by lazy { findViewById(R.id.totalDesireNum) }
    private val totalGoldNum: TextView by lazy { findViewById(R.id.totalGoldNum) }

    private val soundEffectsSetting: LinearLayout by lazy { findViewById(R.id.soundEffectsSettingLayout) }
    private val groupManagerSetting: LinearLayout by lazy { findViewById(R.id.settingGroupManager) }
    private val listStyleSetting: LinearLayout by lazy { findViewById(R.id.settingListStyle) }

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
        scanIcon.setDebouncingClickListener {
            // TODO: open scan
        }
        settingIcon.setDebouncingClickListener {
            ARouter.getInstance().build(HOME_SETTING_ACTIVITY_PATH).navigation()
        }
        totalPlanNum.text = PlanMMKVRepository.planCount.toString()
        totalDesireNum.text = DesireMMKVRepository.desireCount.toString()
        totalGoldNum.text = AccountMMKVRepository.goldCount.toString()

        soundEffectsSetting.setDebouncingClickListener {
            (context as? FragmentActivity)?.supportFragmentManager?.let {
                APieSoundEffectsFragment().show(it, APieSoundEffectsFragment::class.java.simpleName)
            }
        }
        groupManagerSetting.setDebouncingClickListener {
            (context as? FragmentActivity)?.supportFragmentManager?.let {
                APieGroupManagerFragment().show(it, APieGroupManagerFragment::class.java.simpleName)
            }
        }
        listStyleSetting.setDebouncingClickListener {
            // TODO: open list style setting
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