package com.xiaoxun.apie.common.album

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.xiaoxun.apie.common.utils.FilePathConfig.APIE_PRIVATE_DIR
import com.xiaoxun.apie.common.utils.FilePathConfig.MATERIAL_DIR
import com.xiaoxun.apie.common.utils.toast.APieToast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object APieAlbumPickerHelper {
    private var cameraLauncher: ActivityResultLauncher<Intent>? = null
    private var galleryLauncher: ActivityResultLauncher<Intent>? = null
    private var mediaPickerCallback: MediaPickerCallback? = null
    private var currentFile: File? = null // 新增一个用于保存当前文件的属性
    private var cameraPhotoUri: Uri? = null // 添加 cameraPhotoUri 用于保存 URI

    enum class MediaSource {
        CAMERA, GALLERY
    }

    interface MediaPickerCallback {
        fun onMediaSelected(source: MediaSource, uris: List<Uri>)
        fun onCanceled(source: MediaSource)
    }

    fun register(activity: ComponentActivity, callback: MediaPickerCallback) {
        mediaPickerCallback = callback
        cameraLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleCameraResult(result.resultCode, activity)
            }
        galleryLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleGalleryResult(result.resultCode, result.data)
            }
    }

    fun register(fragment: Fragment, callback: MediaPickerCallback) {
        mediaPickerCallback = callback
        cameraLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleCameraResult(result.resultCode, fragment.requireContext())
            }
        galleryLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                handleGalleryResult(result.resultCode, result.data)
            }
    }

    fun showPickerDialog(
        fragmentManager: FragmentManager,
        mediaType: String = "image/*",
        allowMultiple: Boolean = false
    ) {
        APieAlbumPickerDialogFragment.show(fragmentManager, mediaType, allowMultiple)
    }

    fun openCamera(context: Context) {
        // 获取 MATERIAL_DIR 目录
        val materialDir = "$APIE_PRIVATE_DIR/material"
        val file = File(materialDir, "thing_icon_${System.currentTimeMillis()}.jpg")

        // 保存当前文件，以便在结果回调时使用
        currentFile = file

        // 确保文件所在目录存在
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }

        // 使用 FileProvider 获取 content:// URI
        cameraPhotoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",  // 使用你在 AndroidManifest.xml 中设置的 authorities 相同的字符串
            file
        )

        // 启动相机拍照
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)  // 使用 FileProvider 提供的 URI
        }
        cameraLauncher?.launch(intent)
    }

    fun openGallery(mediaType: String = "image/*", allowMultiple: Boolean = false) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = mediaType
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
        }
        galleryLauncher?.launch(Intent.createChooser(intent, "Select Media"))
    }

    private fun handleCameraResult(resultCode: Int, context: Context) {
        currentFile?.let { file ->
            if (resultCode == Activity.RESULT_OK) {
                // 文件已由相机写入，直接使用 FileProvider 的 URI
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                mediaPickerCallback?.onMediaSelected(MediaSource.CAMERA, listOf(uri))
            } else {
                mediaPickerCallback?.onCanceled(MediaSource.CAMERA)
            }
        }
    }

    private fun handleGalleryResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uris = mutableListOf<Uri>()
            data.data?.let { uris.add(it) } // 单选
            data.clipData?.let {
                for (i in 0 until it.itemCount) {
                    uris.add(it.getItemAt(i).uri)
                }
            } // 多选
            if (uris.isNotEmpty()) {
                mediaPickerCallback?.onMediaSelected(MediaSource.GALLERY, uris)
            } else {
                mediaPickerCallback?.onCanceled(MediaSource.GALLERY)
            }
        } else {
            mediaPickerCallback?.onCanceled(MediaSource.GALLERY)
        }
    }
}


