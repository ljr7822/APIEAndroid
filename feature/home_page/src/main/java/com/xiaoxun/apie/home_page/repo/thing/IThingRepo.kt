package com.xiaoxun.apie.home_page.repo.thing

import com.xiaoxun.apie.common_model.home_page.thing.CreateThingInfo

interface IThingRepo {

    suspend fun loadThingGroups(source: ThingGroupSource)

    suspend fun loadThingList()

    suspend fun createThingGroup(groupName: String)

    suspend fun createThing(createThingInfo: CreateThingInfo)

    suspend fun deleteThing(thingId: String)
}

enum class ThingGroupSource {
    INDEX_PAGE,
    CREATE_PAGE
}