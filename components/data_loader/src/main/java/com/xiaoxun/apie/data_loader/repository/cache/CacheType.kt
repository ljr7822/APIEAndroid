package com.xiaoxun.apie.data_loader.repository.cache

/**
 * 缓存策略 目前内存提供三种缓存
 * DB：database缓存，数据是存储在apic_data_cache.db中data_cache中
 * FIlE：文件缓存，数据存放在/sdcard/data/com.xxx.xxx/apic/data_cache
 * SP: sharedPresence缓存，数据是放在data_cache的中
 * 默认是DB缓存，端上可以更改缓存策略，通过DataLoaderConfig.setCacheType(cacheType)
 */
enum class CacheType {
    SP,
    FILE,
    DB
}