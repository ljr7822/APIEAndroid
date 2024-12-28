package com.xiaoxun.apie.home_page.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.utils.dp

class SpaceItemDecoration(private val leftSpace: Int = 12.dp, private val topSpace: Int = 4.dp) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = leftSpace
        outRect.right = leftSpace
        outRect.top = topSpace
        outRect.bottom = topSpace
    }
}