package com.xiaoxun.apie.common.mmkv.property

import com.xiaoxun.apie.common.mmkv.IMMKVOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class MMKVProperty<V>(
    internal val decode: (String) -> V,
    internal val encode: Pair<String, V>.() -> Boolean
) : ReadWriteProperty<IMMKVOwner, V> {
    override fun getValue(thisRef: IMMKVOwner, property: KProperty<*>): V =
        decode(property.name)

    override fun setValue(thisRef: IMMKVOwner, property: KProperty<*>, value: V) {
        encode((property.name) to value)
    }
}