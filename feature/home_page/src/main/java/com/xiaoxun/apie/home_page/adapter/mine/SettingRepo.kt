package com.xiaoxun.apie.home_page.adapter.mine

import com.xiaoxun.apie.common_model.home_page.mine.MineSettingInfo
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.base.APieBaseApplication
import com.xiaoxun.apie.common.utils.AndroidUtils
import com.xiaoxun.apie.common_model.home_page.mine.MineSettingItemAction
import com.xiaoxun.apie.common_model.home_page.mine.MineSettingItemType

object SettingRepo {

    fun buildSettingList(): List<MineSettingInfo> {
        return mutableListOf<MineSettingInfo>().apply {
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_EDIT_PROFILE,
                    R.drawable.apie_mine_edit_icon,
                    "编辑资料",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_ACCOUNT_SETTING,
                    R.drawable.apie_mine_account_icon,
                    "账号设置",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_NOTIFY_SETTING,
                    R.drawable.apie_mine_notify_icon,
                    "通知设置",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_DATA_ANALYSIS,
                    R.drawable.apie_mine_analyze_icon,
                    "数据分析",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_SOUND_EFFECTS,
                    R.drawable.apie_mine_sound_effects_icon,
                    "音效设置",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_GROUP_MANAGER,
                    R.drawable.apie_mine_group_manager_icon,
                    "分组管理",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_LIST_STYLE,
                    R.drawable.apie_mine_list_style_icon,
                    "列表风格",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_STORAGE,
                    R.drawable.apie_mine_cache_icon,
                    "存储空间",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_ABOUT,
                    R.drawable.apie_mine_about_icon,
                    "关于苹果派",
                    MineSettingItemType.ITEM_TYPE_TIP,
                    AndroidUtils.getVersionName(APieBaseApplication.application())
                )
            )
            add(
                MineSettingInfo(
                    MineSettingItemAction.ACTION_LOGOUT,
                    R.drawable.apie_mine_login_out_icon,
                    "退出登录",
                    MineSettingItemType.ITEM_TYPE_NORMAL,
                    ""
                )
            )
        }
    }
}