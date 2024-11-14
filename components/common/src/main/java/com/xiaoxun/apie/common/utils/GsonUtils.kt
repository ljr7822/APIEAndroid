package com.xiaoxun.apie.common.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

object GsonUtils {
    val gson by lazy {
        Gson()
    }

    fun toJson(data: Any): String {
        if (data is JSONObject) {
            return data.toString()
        }
        return gson.toJson(data)
    }

    fun < T> toJson(t: T, type: Type): String {
        return gson.toJson(t, type)
    }

    inline fun <reified T : Any> toListJson(t: List<T>): String {
        return gson.toJson(t, object : TypeToken<List<T>>() {}.type)
    }

    inline fun <reified T : Any> fromJson(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }

    fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson(json, type)
    }

    fun <T> fromJson(json: String, type: Class<T>): T {
        return gson.fromJson(json, type)
    }

    inline fun <reified T : Any> fromListJson(json: String): List<T> {
        return gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
    }
}