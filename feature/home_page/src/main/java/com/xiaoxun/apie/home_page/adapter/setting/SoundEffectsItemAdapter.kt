package com.xiaoxun.apie.home_page.adapter.setting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.repo.SettingMMKVRepository
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_CLICK_DELETE_ID_KEY
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_CLICK_DONE_ID_KEY
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_CLICK_RESET_ID_KEY
import com.xiaoxun.apie.common.utils.hide
import com.xiaoxun.apie.common.utils.setDebouncingClickListener
import com.xiaoxun.apie.common.utils.show
import com.xiaoxun.apie.common.utils.sound_pool.APieSoundPoolHelper
import com.xiaoxun.apie.common_model.setting.SettingInfoType
import com.xiaoxun.apie.common_model.setting.SoundEffectsInfo

class SoundEffectsItemAdapter(
    private val itemMaps: MutableMap<SettingInfoType, MutableList<SoundEffectsInfo>> = mutableMapOf()
) : RecyclerView.Adapter<SoundEffectsItemAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1 // 记录当前选中的位置

    private var items: MutableList<SoundEffectsInfo> = mutableListOf()

    private var mmkvType: SettingInfoType? = null

    init {
        if (itemMaps.containsKey(SettingInfoType.PLAN_DONE_SOUND_EFFECTS)) {
            items = itemMaps[SettingInfoType.PLAN_DONE_SOUND_EFFECTS]!!
            mmkvType = SettingInfoType.PLAN_DONE_SOUND_EFFECTS
        } else if (itemMaps.containsKey(SettingInfoType.PLAN_RESET_SOUND_EFFECTS)) {
            items = itemMaps[SettingInfoType.PLAN_RESET_SOUND_EFFECTS]!!
            mmkvType = SettingInfoType.PLAN_RESET_SOUND_EFFECTS
        } else {
            items = itemMaps[SettingInfoType.PLAN_DELETE_SOUND_EFFECTS]!!
            mmkvType = SettingInfoType.PLAN_DELETE_SOUND_EFFECTS
        }
        val savedId = when(mmkvType) {
            SettingInfoType.PLAN_DONE_SOUND_EFFECTS -> SettingMMKVRepository.planDoneSoundEffectsId
            SettingInfoType.PLAN_RESET_SOUND_EFFECTS -> SettingMMKVRepository.planResetSoundEffectsId
            SettingInfoType.PLAN_DELETE_SOUND_EFFECTS -> SettingMMKVRepository.planDeleteSoundEffectsId
            else -> 0
        }
        selectedPosition = items.indexOfFirst { it.soundInfo.soundId == savedId }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val soundEffectsName: TextView = view.findViewById(R.id.soundEffectsName)
        val soundEffectsSelect: ImageView = view.findViewById(R.id.soundEffectsSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.apie_sound_effects_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // 根据选中状态更新 UI
        if (position == selectedPosition) {
            holder.soundEffectsSelect.show()
            holder.soundEffectsName.apply {
                setTextColor(ContextCompat.getColor(context, R.color.apie_color_6F94F4))
                text = item.soundInfo.soundName
            }
        } else {
            holder.soundEffectsSelect.hide()
            holder.soundEffectsName.apply {
                setTextColor(ContextCompat.getColor(context, R.color.apieTheme_colorBlack_alpha_50))
                text = item.soundInfo.soundName
            }
        }

        // 点击事件
        holder.itemView.setDebouncingClickListener {
            val realPosition = holder.adapterPosition
            if (realPosition != selectedPosition) {
                updateSelectedPosition(realPosition)
                when(mmkvType) {
                    SettingInfoType.PLAN_DONE_SOUND_EFFECTS -> SettingMMKVRepository.planDoneSoundEffectsId = item.soundInfo.soundId
                    SettingInfoType.PLAN_RESET_SOUND_EFFECTS -> SettingMMKVRepository.planResetSoundEffectsId = item.soundInfo.soundId
                    SettingInfoType.PLAN_DELETE_SOUND_EFFECTS -> SettingMMKVRepository.planDeleteSoundEffectsId = item.soundInfo.soundId
                    else -> 0
                }
                APieSoundPoolHelper.playBuiltIn(item.soundInfo)
            }
        }
    }

    private fun updateSelectedPosition(position: Int) {
        val previousSelectedPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelectedPosition) // 更新之前选中的项
        notifyItemChanged(selectedPosition)        // 更新当前选中的项
    }
}