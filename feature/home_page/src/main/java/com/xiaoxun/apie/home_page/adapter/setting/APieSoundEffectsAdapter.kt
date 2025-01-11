package com.xiaoxun.apie.home_page.adapter.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common_model.setting.APieSettingInfo
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.ui.APieSwitch
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common_model.setting.SettingInfoType

class APieSoundEffectsAdapter(
    private val items: MutableList<APieSettingInfo> = mutableListOf()
) : RecyclerView.Adapter<APieSoundEffectsAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    @MainThread
    fun replayData(newData: List<APieSettingInfo>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.itemTitle)
        val titleLayout: LinearLayout = view.findViewById(R.id.title_layout)
        val leftIconView: AppCompatImageView = view.findViewById(R.id.leftIconView)
        val leftTextView: TextView = view.findViewById(R.id.leftTextView)
        val rightTextView: TextView = view.findViewById(R.id.rightTextView)
        val arrow: AppCompatImageView = view.findViewById(R.id.arrow)
        val tick: AppCompatImageView = view.findViewById(R.id.tick)
        val switchCompat: APieSwitch = view.findViewById(R.id.switchCompat)
        val descView: TextView = view.findViewById(R.id.desc)
        val centerTextView: TextView = view.findViewById(R.id.centerTextView)
        val selectedRecyclerView: RecyclerView = view.findViewById(R.id.selectedItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.apie_setting_common_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        item.itemTitle?.let {
            holder.title.show()
            holder.title.text = it
        } ?: holder.title.hide()

        item.desc?.let {
            holder.descView.show()
            holder.descView.text = it
        } ?: holder.descView.hide()

        holder.leftTextView.text = item.leftText

        item.rightText?.let {
            holder.rightTextView.show()
            holder.rightTextView.text = it
        } ?: holder.rightTextView.hide()

        item.centerText?.let {
            holder.centerTextView.show()
            holder.centerTextView.text = it
        } ?: holder.centerTextView.hide()


        item.leftIcon?.let {
            holder.leftIconView.show()
            holder.leftIconView.setImageResource(it)
        } ?: holder.leftIconView.hide()

        item.isShowArrow?.let {
            holder.arrow.visibility = if (it) View.VISIBLE else View.GONE
        } ?: holder.arrow.hide()

        item.isShowTick?.let {
            holder.tick.visibility = if (it) View.VISIBLE else View.GONE
        } ?: holder.tick.hide()

        holder.switchCompat.visibility = if (item.isShowSwitch) View.VISIBLE else View.GONE
        holder.switchCompat.isChecked = item.switchIsCheck ?: false

        holder.selectedRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = SoundEffectsItemAdapter(item.soundEffectsInfoList ?: mutableMapOf())
        }

        if (item.switchIsCheck == true && item.soundEffectsInfoList.isNullOrEmpty().not()) {
            holder.selectedRecyclerView.show()
        } else {
            holder.selectedRecyclerView.hide()
        }
        holder.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            listener?.onSwitchClick(position, item.settingType, isChecked)
            if (isChecked.not()) {
                holder.selectedRecyclerView.hide()
            }else {
                holder.selectedRecyclerView.show()
            }
        }
        holder.itemView.setDebouncingClickListener {
            listener?.onItemClick(position, item.settingType)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, settingInfoType: SettingInfoType)
        fun onSwitchClick(position: Int, settingInfoType: SettingInfoType, isChecked: Boolean)
    }
}
