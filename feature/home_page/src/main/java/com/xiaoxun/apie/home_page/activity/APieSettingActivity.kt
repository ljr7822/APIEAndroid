package com.xiaoxun.apie.home_page.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.xiaoxun.apie.common.HOME_SETTING_ACTIVITY_PATH
import com.xiaoxun.apie.common.base.activity.APieBaseBindingActivity
import com.xiaoxun.apie.home_page.databinding.LayoutApieSettingActivityBinding

@Route(path = HOME_SETTING_ACTIVITY_PATH)
class APieSettingActivity: APieBaseBindingActivity<LayoutApieSettingActivityBinding>(LayoutApieSettingActivityBinding::inflate) {

}