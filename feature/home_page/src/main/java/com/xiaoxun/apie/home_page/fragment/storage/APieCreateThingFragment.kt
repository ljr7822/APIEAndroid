package com.xiaoxun.apie.home_page.fragment.storage

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaoxun.apie.common.album.APieAlbumPickerHelper
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadRoundCornerImage
import com.xiaoxun.apie.common.upload.APieUploadHelper
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common_model.home_page.storage.StatusType
import com.xiaoxun.apie.common_model.home_page.storage.StorageStatusModel
import com.xiaoxun.apie.home_page.adapter.storage.StorageStatusAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieCreateThingFragmentBinding
import com.xiaoxun.apie.home_page.viewmodel.IndexStorageViewModel

class APieCreateThingFragment(
    private val viewModel: IndexStorageViewModel
) :
    APieBaseBottomSheetDialogFragment<LayoutApieCreateThingFragmentBinding>(
        LayoutApieCreateThingFragmentBinding::inflate
    ) {
    companion object {
        private const val TAG = "APieCreateThingFragment"
    }

    private val statusAdapter: StorageStatusAdapter by lazy {
        StorageStatusAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APieAlbumPickerHelper.register(this, object : APieAlbumPickerHelper.MediaPickerCallback {
            override fun onMediaSelected(
                source: APieAlbumPickerHelper.MediaSource,
                uris: List<Uri>
            ) {
                val uri = uris.firstOrNull() ?: return
                APieLog.d(TAG, "${source.name} selected: $uri")

                val url = APieUploadHelper.uploadImage(requireActivity(), uri)
                viewModel.updateThingIconUrl(url)
            }

            override fun onCanceled(source: APieAlbumPickerHelper.MediaSource) {
                APieLog.d(TAG, "${source.name} selection canceled")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    private fun initView() {
        initThingIconView()
        initStatusView()
    }

    private fun initThingIconView() {
        binding.thingIcon.setDebouncingClickListener {
            openPhotoPicker()
        }
    }

    private fun initStatusView() {
        val status = mutableListOf(
            StorageStatusModel(StatusType.USEING),
            StorageStatusModel(StatusType.RETIRED),
            StorageStatusModel(StatusType.DAMAGED),
            StorageStatusModel(StatusType.LOST),
            StorageStatusModel(StatusType.DUSTY),
            StorageStatusModel(StatusType.SOLD)
        )
        statusAdapter.updateData(status)
        binding.statusRecyclerView.apply {
            adapter = statusAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initObserver() {
        viewModel.thingIconUrl.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                return@observe
            }
            setThingIcon(it)
        }
    }

    private fun openPhotoPicker() {
        APieAlbumPickerHelper.showPickerDialog(parentFragmentManager)
    }

    private fun setThingIcon(url: String) {
        binding.addIcon.hide()
        binding.thingIcon.loadRoundCornerImage(
            requireActivity(),
            url,
            12.dp
        )
    }
}