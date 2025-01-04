package com.xiaoxun.apie.home_page.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.utils.dp

class SpaceItemDecoration(
    private val leftSpace: Int = 0.dp,
    private val rightSpace: Int = 6.dp,
    private val topSpace: Int = 0.dp,
    private val bottomSpace: Int = 0.dp
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = leftSpace
        outRect.right = rightSpace
        outRect.top = topSpace
        outRect.bottom = bottomSpace
    }
}