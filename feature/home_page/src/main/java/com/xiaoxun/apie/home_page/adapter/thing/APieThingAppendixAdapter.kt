package com.xiaoxun.apie.home_page.adapter.thing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.ui.easy_glide.APieEasyImage.loadRoundCornerImage
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.home_page.R

class APieThingAppendixAdapter(
    private val context: Context,
    private val dataList: MutableList<String> = mutableListOf(),
    private val onAddClick: () -> Unit,
    private val onItemClick: (position: Int, url: String) -> Unit = { _, _ -> },
    private val onDeleteClick: (position: Int, url: String) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_ADD = 1
    }

    // 数据操作相关方法
    fun updateData(newData: List<String>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    fun addItem(url: String) {
        dataList.add(url)
        notifyItemInserted(dataList.size - 1)
    }

    fun removeItem(position: Int) {
        if (position in 0 until dataList.size) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = dataList.size + 1 // 始终保留一个添加按钮

    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size) VIEW_TYPE_ADD else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_apie_appendix_image_item, parent, false)
                ImageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_apie_appendix_add_item, parent, false)
                AddButtonViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> {
                val url = dataList[position]
                holder.bind(url)
            }
            is AddButtonViewHolder -> holder.bind()
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        private val deleteIconLayout: View = itemView.findViewById(R.id.deleteIconLayout)
        private val deleteIcon: View = itemView.findViewById(R.id.deleteIcon)

        fun bind(url: String) {
            // 使用你的扩展方法加载图片
            ivImage.loadRoundCornerImage(
                context = context,
                url = url,
                radius = 12.dp
            )

            deleteIcon.setOnClickListener {
                deleteIconLayout.hide()
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClick(position, url)
                }
            }

            itemView.setOnClickListener {
                deleteIconLayout.hide()
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position, url)
                }
            }

            itemView.setOnLongClickListener {
                deleteIconLayout.show()
                true
            }
        }
    }

    inner class AddButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener { onAddClick() }
        }
    }
}