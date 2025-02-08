package com.xiaoxun.apie.common.album


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentManager
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.databinding.LayoutApieAlbumSelectFragmentBinding
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.setDebouncingClickListener

class APieAlbumPickerDialogFragment(
    private var mediaType: String = DEF_MEDIA_TYPE,
    private var allowMultiple: Boolean = false
) : APieBaseBottomSheetDialogFragment<LayoutApieAlbumSelectFragmentBinding>(
    LayoutApieAlbumSelectFragmentBinding::inflate
) {

    companion object {
        const val TAG = "APieAlbumPickerDialogFragment"
        const val DEF_MEDIA_TYPE = "image/*"

        fun show(fragmentManager: FragmentManager, mediaType: String = DEF_MEDIA_TYPE, allowMultiple: Boolean = false) {
            APieAlbumPickerDialogFragment(mediaType, allowMultiple)
                .show(fragmentManager, TAG)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableCancel = true
        super.onCreate(savedInstanceState)
        val screenHeight = UIUtils.getScreenRealHeight(requireContext())
        peekHeight = screenHeight / 4
        layoutHeight = WRAP_CONTENT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.openAlbum.setDebouncingClickListener {
            dismiss()
            APieAlbumPickerHelper.openGallery(mediaType, allowMultiple)
        }

        binding.openCamera.setDebouncingClickListener {
            dismiss()
            APieAlbumPickerHelper.openCamera(requireContext())
        }

        binding.cancel.setDebouncingClickListener {
            dismiss()
        }
    }
}
