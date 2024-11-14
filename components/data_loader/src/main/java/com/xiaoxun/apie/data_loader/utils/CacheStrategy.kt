package com.xiaoxun.apie.data_loader.utils

/**
 * 缓存策略
 * FORCE_NET：强制网络，如果网络没有数据，或者无网的情况会返回失败
 * FORCE_CACHE：强制缓存，如果没有缓存数据，返回失败，
 * FIRST_NET：优先网络，先请求网络，如果返回失败，再尝试从缓存中取，如果有，则返回成功，如果没有，则返回失败
 * FIRST_CACHE：优先缓存，先请求缓存，如果返回失败，在请求网络，如果有，则返回成功，如果没有，则返回失败
 * FIRST_CACHE_AND_SEND_REQUEST：优先缓存，请求缓存的时候，会发送请求，如果缓存成功，则第一次返回成功，如果网络请求成功，会返回第二次成功，如果都没有，就返回失败，目前capa内部使用最多请求方式
 * 注意：请求缓存的时候，优先取的都内存缓存，其次磁盘缓存，而且内存数据生命周期较长，如同一面板打开，不发送请求的话，基本都是一份内存缓存，非必要不要写一个临时状态信息
 */
enum class CacheStrategy {
    /**
     * 强制网络
     */
    FORCE_NET,

    /**
     * 强制缓存
     */
    FORCE_CACHE,

    /**
     * 强制内存缓存
     */
    FORCE_MEMORY_CACHE,

    /**
     * 优先网络
     */
    FIRST_NET,

    /**
     * 优先缓存
     */
    FIRST_CACHE,

    /**
     * 优先缓存，同时会发送一次请求，请求结果和获取缓存均会触发一次onNext, 会收到两次onNext
     */
    FIRST_CACHE_AND_SEND_REQUEST,

    /**
     * 优先缓存，同时会发送一次请求，但只会收到一次onNext
     * 1、无缓存时候，onNext为网络结果
     * 2、有缓存时候，onNext为缓存结果
     */
    FIRST_CACHE_AND_REQUEST_NET
}