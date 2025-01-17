package com.xiaoxun.apie.common.ui.easy_glide.progress

interface OnProgressListener {
    fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long)
}