package com.xiaoxun.apie.common.utils.cache

import android.content.Context
import java.io.File
import java.text.DecimalFormat

object APieCacheUtils {

    /**
     * 获取应用缓存大小，包括 files 目录
     * @param context 上下文对象
     * @return 缓存大小的字符串表示
     */
    fun getCacheAndFilesSize(context: Context): String {
        val cacheDir = context.cacheDir // 内部缓存路径
        val externalCacheDir = context.externalCacheDir // 外部缓存路径 (可能为 null)
        val filesDir = context.filesDir // 应用的 files 目录

        var totalSize = getFolderSize(cacheDir)
        if (externalCacheDir != null) {
            totalSize += getFolderSize(externalCacheDir)
        }
        totalSize += getFolderSize(filesDir)

        return formatSize(totalSize)
    }

    /**
     * 清理应用缓存和 files 目录
     * @param context 上下文对象
     */
    fun clearCacheAndFiles(context: Context) {
        deleteFolder(context.cacheDir)
        context.externalCacheDir?.let { deleteFolder(it) }
        deleteFolder(context.filesDir)
    }

    // 获取文件夹大小
    private fun getFolderSize(file: File?): Long {
        if (file == null || !file.exists()) return 0
        return if (file.isDirectory) {
            file.listFiles()?.sumOf { getFolderSize(it) } ?: 0
        } else {
            file.length()
        }
    }

    // 删除文件夹及其内容
    private fun deleteFolder(file: File?): Boolean {
        if (file == null || !file.exists()) return true
        if (file.isDirectory) {
            file.listFiles()?.forEach { deleteFolder(it) }
        }
        return file.delete()
    }

    // 格式化文件大小
    private fun formatSize(size: Long): String {
        if (size <= 0) return "0B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        val formatter = DecimalFormat("#,##0.##")
        return "${formatter.format(size / Math.pow(1024.0, digitGroups.toDouble()))} ${units[digitGroups]}"
    }
}