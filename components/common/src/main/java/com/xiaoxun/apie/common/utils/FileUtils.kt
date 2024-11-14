package com.xiaoxun.apie.common.utils

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

object FileUtils {
    fun readFileContent(filePath: String): String {
        return readFileContent(File(filePath))
    }

    fun readFileContent(file: File): String {
        val buf = StringBuilder()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8"))
            var str: String?
            while (reader.readLine().also { str = it } != null) {
                buf.append(str)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return buf.toString()
    }


    fun writeContentToFile(destFile: File, content: String) {
        destFile.writeText(content)
    }

    fun exist(path: String): Boolean {
        return kotlin.runCatching { File(path).exists() }.getOrDefault(false)
    }

    fun sizeOf(file: File): Long {
        return file.length()
    }

    fun sizeOf(path: String?): Long {
        path ?: return 0
        return File(path).length()
    }
}