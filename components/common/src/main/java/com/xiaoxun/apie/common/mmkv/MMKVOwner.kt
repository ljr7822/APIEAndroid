package com.xiaoxun.apie.common.mmkv

import android.os.Parcelable
import androidx.lifecycle.LiveData
import com.tencent.mmkv.MMKV
import com.xiaoxun.apie.common.mmkv.property.MMKVLiveDataProperty
import com.xiaoxun.apie.common.mmkv.property.MMKVMapProperty
import com.xiaoxun.apie.common.mmkv.property.MMKVProperty
import com.xiaoxun.apie.common.mmkv.property.MMKVStateFlowProperty
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

open class MMKVOwner(override val mmapID: String) : IMMKVOwner {
    override val kv: MMKV by lazy { MMKV.mmkvWithID(mmapID) }
}

interface IMMKVOwner {
    val mmapID: String

    val kv: MMKV

    fun mmkvInt(default: Int = 0) =
        MMKVProperty({ kv.decodeInt(it, default) }, { kv.encode(first, second) })

    fun mmkvLong(default: Long = 0L) =
        MMKVProperty({ kv.decodeLong(it, default) }, { kv.encode(first, second) })

    fun mmkvBool(default: Boolean = false) =
        MMKVProperty({ kv.decodeBool(it, default) }, { kv.encode(first, second) })

    fun mmkvFloat(default: Float = 0f) =
        MMKVProperty({ kv.decodeFloat(it, default) }, { kv.encode(first, second) })

    fun mmkvDouble(default: Double = 0.0) =
        MMKVProperty({ kv.decodeDouble(it, default) }, { kv.encode(first, second) })

    fun mmkvString() =
        MMKVProperty({ kv.decodeString(it) }, { kv.encode(first, second) })

    fun mmkvString(default: String) =
        MMKVProperty({ kv.decodeString(it) ?: default }, { kv.encode(first, second) })

    fun mmkvStringSet() =
        MMKVProperty({ kv.decodeStringSet(it) }, { kv.encode(first, second) })

    fun mmkvStringSet(default: Set<String>) =
        MMKVProperty({ kv.decodeStringSet(it) ?: default }, { kv.encode(first, second) })

    fun mmkvBytes() =
        MMKVProperty({ kv.decodeBytes(it) }, { kv.encode(first, second) })

    fun mmkvBytes(default: ByteArray) =
        MMKVProperty({ kv.decodeBytes(it) ?: default }, { kv.encode(first, second) })

    fun <V> MMKVProperty<V>.asLiveData() = MMKVLiveDataProperty(this)

    fun <V> MMKVProperty<V>.asStateFlow() = MMKVStateFlowProperty(this)

    fun <V> MMKVProperty<V>.asMap() = MMKVMapProperty(this)

    fun clearAllKV() = kv.clearAll()
}

inline fun <reified T : Parcelable> IMMKVOwner.mmkvParcelable() =
    MMKVProperty({ kv.decodeParcelable(it, T::class.java) }, { kv.encode(first, second) })

inline fun <reified T : Parcelable> IMMKVOwner.mmkvParcelable(default: T) =
    MMKVProperty({ kv.decodeParcelable(it, T::class.java) ?: default }, { kv.encode(first, second) })

fun IMMKVOwner.getAllKV(): Map<String, Any?> = buildMap {
    val types = arrayOf(MMKVProperty::class, MMKVLiveDataProperty::class, MMKVStateFlowProperty::class, MMKVMapProperty::class)
    this@getAllKV::class.memberProperties
        .filterIsInstance<KProperty1<IMMKVOwner, *>>()
        .forEach { property ->
            property.isAccessible = true
            val delegate = property.getDelegate(this@getAllKV)
            if (types.any { it.isInstance(delegate) }) {
                this[property.name] = when (val value = property.get(this@getAllKV)) {
                    is LiveData<*> -> value.value
                    is StateFlow<*> -> value.value
                    else -> value
                }
            }
            property.isAccessible = false
        }
}