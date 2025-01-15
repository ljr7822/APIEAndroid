package com.xiaoxun.apie.common.mmkv.property

import com.xiaoxun.apie.common.mmkv.IMMKVOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MMKVStateFlowProperty<V>(private val mmkvProperty: MMKVProperty<V>) : ReadOnlyProperty<IMMKVOwner, MutableStateFlow<V>> {
    private var cache: MutableStateFlow<V>? = null

    override fun getValue(thisRef: IMMKVOwner, property: KProperty<*>): MutableStateFlow<V> =
        cache ?: MMKVFlow(
            { mmkvProperty.getValue(thisRef, property) },
            { mmkvProperty.setValue(thisRef, property, it) }
        ).also { cache = it }
}

class MMKVFlow<V>(
    private val getMMKVValue: () -> V,
    private val setMMKVValue: (V) -> Unit,
    private val flow: MutableStateFlow<V> = MutableStateFlow(getMMKVValue())
) : MutableStateFlow<V> by flow {
    override var value: V
        get() = getMMKVValue()
        set(value) {
            val origin = flow.value
            flow.value = value
            if (origin != value) {
                setMMKVValue(value)
            }
        }

    override fun compareAndSet(expect: V, update: V): Boolean =
        flow.compareAndSet(expect, update).also { setSuccess ->
            if (setSuccess) setMMKVValue(value)
        }
}