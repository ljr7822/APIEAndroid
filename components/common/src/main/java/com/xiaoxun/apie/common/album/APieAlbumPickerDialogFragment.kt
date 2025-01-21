package com.xiaoxun.apie.common.album


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.result.ActivityResultLauncher
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.databinding.LayoutApieAlbumSelectFragmentBinding
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.setDebouncingClickListener

class APieAlbumPickerDialogFragment(
    private var mediaType: String = DEF_MEDIA_TYPE,
    private var allowMultiple: Boolean = false,
    private var launcher: ActivityResultLauncher<Intent>
) :
    APieBaseBottomSheetDialogFragment<LayoutApieAlbumSelectFragmentBinding>(
        LayoutApieAlbumSelectFragmentBinding::inflate
    ) {

    companion object {
        const val TAG = "APieAlbumPickerDialogFragment"
        const val DEF_MEDIA_TYPE = "image/*"
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
            APieAlbumPickerHelper.openGallery(
                context = requireContext(),
                launcher = launcher,
                mediaType = mediaType,
                allowMultiple = allowMultiple
            )
        }

        binding.openCamera.setDebouncingClickListener {
            dismiss()
            APieAlbumPickerHelper.openCamera(
                context = requireContext(),
                launcher = launcher
            )
        }

        binding.cancel.setDebouncingClickListener {
            dismiss()
        }
    }
}