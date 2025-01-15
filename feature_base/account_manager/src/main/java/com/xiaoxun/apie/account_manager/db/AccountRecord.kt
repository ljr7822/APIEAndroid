package com.xiaoxun.apie.account_manager.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用户的接口回调数据模型
 * {
 *   "userId": "99678322425856",
 *   "userName": "☀️男孩",
 *   "userPortrait": "https://picasso-static.xiaohongshu.com/fe-platform/ad1ed1f24ebad49ce8ebf2f82acd63b8aaf00470.png",
 *   "phoneNum": "18289816889",
 *   "token": "MDgxOTM4NmMtYmU0MS00MTc2LThlOWEtYzMxMmVlMzg4OWFi",
 *   "desc": "人生若只如初见， 何事秋风悲画扇",
 *   "sex": 0,
 *   "address": "中国-北京-朝阳区",
 *   "school": "清华大学",
 *   "birthday": "2025-01-01T02:45:36.000+00:00",
 *   "grade": 1000,
 *   "goldCount": 9999,
 *   "userType": 10,
 *   "totalPlan": null,
 *   "totalDesire": null
 * }
 */
/**
 * 数据缓存表
 */
@Entity(tableName = "account_data")
class AccountRecord {
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var userId: String = ""

    @ColumnInfo(name = "user_name")
    var userName: String = ""

    @ColumnInfo(name = "user_portrait")
    var userPortrait: String? = ""

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String = ""

    @ColumnInfo(name = "token")
    var token: String = ""

    @ColumnInfo(name = "desc")
    var desc: String? = ""

    @ColumnInfo(name = "sex")
    var sex: Int = 0

    @ColumnInfo(name = "address")
    var address: String? = ""

    @ColumnInfo(name = "grade")
    var grade: Int = 0

    @ColumnInfo(name = "user_type")
    var userType: Int = 0
}