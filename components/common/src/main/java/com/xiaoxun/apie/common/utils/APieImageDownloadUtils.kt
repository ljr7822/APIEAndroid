package com.xiaoxun.apie.common.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.base.APieBaseApplication
import com.xiaoxun.apie.common.utils.toast.APieToast
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object APieImageDownloadUtils {
    private const val TAG = "APieImageDownloadUtils"

    private val externalFileDir =
        APieBaseApplication.application().getExternalFilesDir(null)?.absolutePath
    private val externalAPPDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        "$externalFileDir/APIE"
    } else {
        Environment.getExternalStorageDirectory().resolve("APIE").absolutePath
    }


    /**
     * 下载图片并在媒体库中显示（兼容 Android 各版本）
     * @param context 上下文
     * @param imgUrl 图片 URL
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    fun downloadImageToGallery(context: Context, imgUrl: String?) {
        val extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl) ?: "jpg"
        Observable.create { emitter: ObservableEmitter<File?> ->
            try {
                val file = Glide.with(context).download(imgUrl).submit().get()
                val fileName = "apie_${System.currentTimeMillis()}.$extension"
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveImageToMediaStore(context, file, fileName, mimeType, emitter)
                } else {
                    saveImageToPublicDirectory(context, file, fileName, mimeType, emitter)
                }
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                APieToast.showDialog(R.string.easy_glide_save_succss)
            }) {
                APieToast.showDialog(R.string.easy_glide_save_failed)
                APieLog.e(TAG, "downloadImageToGallery error: ${it.message}")
            }
    }

    private fun saveImageToMediaStore(
        context: Context,
        file: File,
        fileName: String,
        mimeType: String?,
        emitter: ObservableEmitter<File?>
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/APIE")
        }
        val resolver = context.contentResolver
        val uri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it).use { fos ->
                FileInputStream(file).use { fis ->
                    val buffer = ByteArray(1024 * 8)
                    var bytesRead: Int
                    while (fis.read(buffer).also { bytesRead = it } != -1) {
                        fos?.write(buffer, 0, bytesRead)
                    }
                }
            }
            emitter.onNext(File(uri.toString()))
        } ?: run {
            emitter.onError(Exception("Failed to insert media content"))
        }
    }

    private fun saveImageToPublicDirectory(
        context: Context,
        file: File,
        fileName: String,
        mimeType: String?,
        emitter: ObservableEmitter<File?>
    ) {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/APIE")
        if (!directory.exists()) directory.mkdirs()

        val targetFile = File(directory, fileName)
        FileInputStream(file).use { fis ->
            FileOutputStream(targetFile).use { fos ->
                val buffer = ByteArray(1024 * 8)
                var bytesRead: Int
                while (fis.read(buffer).also { bytesRead = it } != -1) {
                    fos.write(buffer, 0, bytesRead)
                }
            }
        }
        MediaScannerConnection.scanFile(
            context,
            arrayOf(targetFile.absolutePath),
            arrayOf(mimeType),
            null
        )
        emitter.onNext(targetFile)
    }

    /**
     * 下载图片到应用的专属目录下
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    fun downloadImageToAppDir(context: Context, imgUrl: String?) {
        val extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl)
        Observable.create { emitter: ObservableEmitter<File?> ->
            // Glide提供了一个download() 接口来获取缓存的图片文件，
            // 但是前提必须要设置diskCacheStrategy方法的缓存策略为
            // DiskCacheStrategy.ALL或者DiskCacheStrategy.SOURCE，
            // 还有download()方法需要在子线程里进行
            val file = Glide.with(context).download(imgUrl).submit().get()
            APieLog.d(
                "APieEasyGlide",
                "downloadImageToGallery: $file, externalAPPDir=${externalAPPDir}"
            )
            val appDir = File(externalAPPDir)
            if (!appDir.exists()) {
                appDir.mkdirs()
            }
            // 获得原文件流
            val fis = FileInputStream(file)
            // 保存的文件名
            val fileName = "apie_" + System.currentTimeMillis() + "." + extension
            // 目标文件
            val targetFile = File(appDir, fileName)
            // 输出文件流
            val fos = FileOutputStream(targetFile)
            // 缓冲数组
            val b = ByteArray(1024 * 8)
            while (fis.read(b) != -1) {
                fos.write(b)
            }
            fos.flush()
            fis.close()
            fos.close()
            // 扫描媒体库
            val mimeTypes = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            MediaScannerConnection.scanFile(
                context,
                arrayOf(targetFile.absolutePath),
                arrayOf(mimeTypes),
                null
            )
            emitter.onNext(targetFile)
        }.subscribeOn(Schedulers.io()) // 发送事件在io线程
            .observeOn(AndroidSchedulers.mainThread()) // 最后切换主线程提示结果
            .subscribe({
                APieToast.showDialog(R.string.easy_glide_save_succss)
            }
            ) {
                APieToast.showDialog(R.string.easy_glide_save_failed)
                APieLog.e("APieEasyGlide", "downloadImageToGallery error: ${it.message}")
            }
    }

    @WorkerThread
    fun saveToPublic(
        src: File,
        publicDir: String,
        subPath: String,
        move: Boolean,
        mimeType: String?
    ): Boolean {
        return try {
            var ret = false
            var dst: File? = null
            val values = ContentValues()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, subPath)

                if ((Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) &&
                    !(Environment.DIRECTORY_DCIM == publicDir || Environment.DIRECTORY_MOVIES == publicDir)
                ) {
                    // On HarmonyOS, only DCIM and Movies directories are supported. Use DCIM for others.
                    values.put(
                        MediaStore.Images.ImageColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_DCIM
                    )
                } else {
                    values.put(MediaStore.Images.ImageColumns.RELATIVE_PATH, publicDir)
                }

                mimeType?.let { values.put(MediaStore.Images.Media.MIME_TYPE, it) }

                var store = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                if (mimeType?.startsWith("video") == true) {
                    store = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }

                val uri = APieBaseApplication.application().contentResolver.insert(store, values)
                uri?.let {
                    ret = writeToFile(it, FileInputStream(src))
                    if (ret) {
                        APieBaseApplication.application()
                            .sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, it))
                    }
                    if (move) {
                        src.delete()
                    }
                }
            } else {
                // For Android versions below 10, direct file path handling.
                dst = File(Environment.getExternalStoragePublicDirectory(publicDir), subPath)
                ret = copyOrMoveFile(src, dst, move)
                if (ret) {
                    values.put(MediaStore.Images.Media.DATA, dst.absolutePath)
                    mimeType?.let { values.put(MediaStore.Images.Media.MIME_TYPE, it) }

                    var store = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    if (mimeType?.startsWith("video") == true) {
                        store = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }

                    APieBaseApplication.application().contentResolver.insert(store, values)
                    APieBaseApplication.application().sendBroadcast(
                        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dst))
                    )
                }
            }
            ret
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun writeToFile(uri: Uri, inputStream: FileInputStream): Boolean {
        return try {
            APieBaseApplication.application().contentResolver.openOutputStream(uri)
                ?.use { outputStream ->
                    inputStream.use { input ->
                        input.copyTo(outputStream)
                    }
                }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun copyOrMoveFile(src: File, dst: File, move: Boolean): Boolean {
        return try {
            src.copyTo(dst, overwrite = true)
            if (move) src.delete()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}