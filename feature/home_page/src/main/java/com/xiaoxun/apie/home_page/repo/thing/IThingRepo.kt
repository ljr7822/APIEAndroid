package com.xiaoxun.apie.home_page.repo.thing

interface IThingRepo {

    suspend fun loadThingGroups(source: ThingGroupSource)

    suspend fun loadThingList()

    suspend fun createThingGroup(groupName: String)
}

enum class ThingGroupSource {
    INDEX_PAGE,
    CREATE_PAGE
}