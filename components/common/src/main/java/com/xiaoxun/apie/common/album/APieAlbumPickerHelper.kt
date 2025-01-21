package com.xiaoxun.apie.common.album

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object APieAlbumPickerHelper {
    private const val DEF_MAX_SELECT_COUNT = 10

    interface MediaPickerCallback {
        fun onMediaSelected(uris: List<Uri>)
        fun onCanceled()
    }

    var callback: MediaPickerCallback? = null

    fun registerPicker(
        activity: Activity,
        fragment: Fragment? = null,
        callback: MediaPickerCallback
    ): ActivityResultLauncher<Intent>? {
        this.callback = callback
        return fragment?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleResult(result.resultCode, result.data)
        }
            ?: (activity as? androidx.activity.ComponentActivity)?.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                handleResult(result.resultCode, result.data)
            }
    }

    fun showPickerDialog(
        fragmentManager: FragmentManager,
        launcher: ActivityResultLauncher<Intent>,
        mediaType: String = "image/*",
        allowMultiple: Boolean = false
    ) {
        APieAlbumPickerDialogFragment(mediaType, allowMultiple, launcher).show(
            fragmentManager,
            APieAlbumPickerDialogFragment::class.java.simpleName
        )
    }

    fun openGallery(
        context: Context,
        launcher: ActivityResultLauncher<Intent>,
        mediaType: String,
        allowMultiple: Boolean
    ) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(Intent.ACTION_PICK).apply {
                type = mediaType
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
            }
        } else {
            Intent(Intent.ACTION_PICK).apply {
                type = mediaType
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
            }
        }
        launcher.launch(Intent.createChooser(intent, "Select Media"))
    }

    fun openCamera(context: Context, launcher: ActivityResultLauncher<Intent>) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcher.launch(intent)
    }

    private fun handleResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uris = mutableListOf<Uri>()
            data.data?.let { uris.add(it) } // Single select
            data.clipData?.let {
                for (i in 0 until it.itemCount) {
                    uris.add(it.getItemAt(i).uri)
                }
            } // Multi-select
            if (uris.isNotEmpty()) {
                callback?.onMediaSelected(uris)
            } else {
                callback?.onCanceled()
            }
        } else {
            callback?.onCanceled()
        }
    }
}
