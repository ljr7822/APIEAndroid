package com.xiaoxun.apie.common.db

import androidx.room.InvalidationTracker

abstract class APieObserver(firstTable: String, vararg rest: String) : InvalidationTracker.Observer(firstTable, *rest) {

    override fun onInvalidated(tables: Set<String>) {
        onChange(tables)
    }

    abstract fun onChange(tables: Set<String>)
}