package com.xiaoxun.apie.common.mmkv.property

import androidx.lifecycle.MutableLiveData
import com.xiaoxun.apie.common.mmkv.IMMKVOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MMKVLiveDataProperty<V>(private val mmkvProperty: MMKVProperty<V>) : ReadOnlyProperty<IMMKVOwner, MutableLiveData<V>> {
    private var cache: MutableLiveData<V>? = null

    override fun getValue(thisRef: IMMKVOwner, property: KProperty<*>): MutableLiveData<V> =
        cache ?: MMKVLiveData(
            { mmkvProperty.getValue(thisRef, property) },
            { mmkvProperty.setValue(thisRef, property, it) }
        ).also { cache = it }
}

class MMKVLiveData<V>(
    private val getMMKVValue: () -> V,
    private val setMMKVValue: (V) -> Unit
) : MutableLiveData<V>(getMMKVValue()) {
    override fun getValue() = getMMKVValue()

    override fun setValue(value: V) {
        super.setValue(value)
        setMMKVValue(value)
    }
}