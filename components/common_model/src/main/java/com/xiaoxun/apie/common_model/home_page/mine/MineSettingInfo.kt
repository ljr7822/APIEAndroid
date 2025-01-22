package com.xiaoxun.apie.common_model.home_page.mine

data class MineSettingInfo (
    var mineSettingItemAction: MineSettingItemAction,
    var itemLeftIcon: Int = 0,
    var itemTitle: String = "",
    var itemType: MineSettingItemType = MineSettingItemType.ITEM_TYPE_NORMAL,
    var itemTip: String = "",
)

enum class MineSettingItemType {
    ITEM_TYPE_NORMAL, // 右侧箭头
    ITEM_TYPE_SWITCH, // 带开关
    ITEM_TYPE_TIP // 文案提示
}

// item设置类型
enum class MineSettingItemAction {
    ACTION_EDIT_PROFILE,
    ACTION_ACCOUNT_SETTING,
    ACTION_NOTIFY_SETTING,
    ACTION_DATA_ANALYSIS,
    ACTION_SOUND_EFFECTS,
    ACTION_GROUP_MANAGER,
    ACTION_LIST_STYLE,
    ACTION_STORAGE,
    ACTION_ABOUT,
    ACTION_LOGOUT
}