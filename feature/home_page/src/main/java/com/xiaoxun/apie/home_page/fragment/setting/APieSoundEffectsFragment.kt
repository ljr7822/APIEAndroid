package com.xiaoxun.apie.home_page.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaoxun.apie.common.base.fragment.APieBaseBottomSheetDialogFragment
import com.xiaoxun.apie.common.repo.SettingMMKVRepository
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_DELETE_PLAN_SWITCH_KEY
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_DONE_PLAN_SWITCH_KEY
import com.xiaoxun.apie.common.utils.SharedPreferencesHelper.SP_ACCOUNT_SOUND_EFFECTS_RESET_PLAN_SWITCH_KEY
import com.xiaoxun.apie.common.utils.UIUtils
import com.xiaoxun.apie.common.utils.dp
import com.xiaoxun.apie.common.utils.sound_pool.SoundInfo
import com.xiaoxun.apie.common_model.setting.APieSettingInfo
import com.xiaoxun.apie.common_model.setting.SettingInfoType
import com.xiaoxun.apie.common_model.setting.SoundEffectsInfo
import com.xiaoxun.apie.home_page.adapter.setting.APieSoundEffectsAdapter
import com.xiaoxun.apie.home_page.databinding.LayoutApieSoundEffectsSettingFragmentBinding

/**
 * 声音效果设置
 */
class APieSoundEffectsFragment :
    APieBaseBottomSheetDialogFragment<LayoutApieSoundEffectsSettingFragmentBinding>(
        LayoutApieSoundEffectsSettingFragmentBinding::inflate
    ) {

    private val soundEffectsAdapter: APieSoundEffectsAdapter by lazy {
        APieSoundEffectsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableCancel = true
        super.onCreate(savedInstanceState)
        val screenHeight = UIUtils.getScreenRealHeight(requireContext())
        peekHeight = screenHeight / 2
        layoutHeight = screenHeight / 2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val list = mutableListOf(
            APieSettingInfo(
                settingType = SettingInfoType.PLAN_DONE_SOUND_EFFECTS,
                leftIcon = com.xiaoxun.apie.common.R.drawable.apie_setting_sound_effects_done_icon,
                leftText = "打卡音效",
                desc = "开启后，打卡将会触发音效，选择自己喜欢的音效吧～",
                isShowSwitch = true,
                switchIsCheck = SettingMMKVRepository.planDoneSoundEffectsSwitch,
                soundEffectsInfoList = mutableMapOf(
                    SettingInfoType.PLAN_DONE_SOUND_EFFECTS to mutableListOf(
                        SoundEffectsInfo(SoundInfo.WATER_DROPLETS),
                        SoundEffectsInfo(SoundInfo.SLOWER),
                        SoundEffectsInfo(SoundInfo.CATCH_ON),
                        SoundEffectsInfo(SoundInfo.SHAKE),
                        SoundEffectsInfo(SoundInfo.NEW_MESSAGE),
                        SoundEffectsInfo(SoundInfo.END_TIP)
                    )
                )
            ),
            APieSettingInfo(
                settingType = SettingInfoType.PLAN_RESET_SOUND_EFFECTS,
                leftIcon = com.xiaoxun.apie.common.R.drawable.apie_setting_sound_effects_reset_icon,
                leftText = "撤销打卡音效",
                desc = "开启后，撤销打卡将会触发音效～",
                isShowSwitch = true,
                switchIsCheck = SettingMMKVRepository.planResetSoundEffectsSwitch,
                soundEffectsInfoList = mutableMapOf(
                    SettingInfoType.PLAN_RESET_SOUND_EFFECTS to mutableListOf(
                        SoundEffectsInfo(SoundInfo.WATER_DROPLETS),
                        SoundEffectsInfo(SoundInfo.SLOWER),
                        SoundEffectsInfo(SoundInfo.CATCH_ON),
                        SoundEffectsInfo(SoundInfo.SHAKE),
                        SoundEffectsInfo(SoundInfo.NEW_MESSAGE),
                        SoundEffectsInfo(SoundInfo.END_TIP)
                    )
                )
            ),
            APieSettingInfo(
                settingType = SettingInfoType.PLAN_DELETE_SOUND_EFFECTS,
                leftIcon = com.xiaoxun.apie.common.R.drawable.apie_setting_sound_effects_delete_icon,
                leftText = "删除任务音效",
                desc = "开启后，删除任务将会触发音效～",
                isShowSwitch = true,
                switchIsCheck = SettingMMKVRepository.planDeleteSoundEffectsSwitch,
                soundEffectsInfoList = mutableMapOf(
                    SettingInfoType.PLAN_DELETE_SOUND_EFFECTS to mutableListOf(
                        SoundEffectsInfo(SoundInfo.WATER_DROPLETS),
                        SoundEffectsInfo(SoundInfo.SLOWER),
                        SoundEffectsInfo(SoundInfo.CATCH_ON),
                        SoundEffectsInfo(SoundInfo.SHAKE),
                        SoundEffectsInfo(SoundInfo.NEW_MESSAGE),
                        SoundEffectsInfo(SoundInfo.END_TIP)
                    )
                )
            )
        )
        binding.soundEffectsCardView.layoutParams.apply {
            height = UIUtils.getScreenRealHeight(requireContext()) / 2 - 80.dp
        }
        soundEffectsAdapter.replayData(list)
        soundEffectsAdapter.setOnItemClickListener(object :
            APieSoundEffectsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, settingInfoType: SettingInfoType) {}
            override fun onSwitchClick(
                position: Int,
                settingInfoType: SettingInfoType,
                isChecked: Boolean
            ) {
                when (settingInfoType) {
                    SettingInfoType.PLAN_DONE_SOUND_EFFECTS -> SettingMMKVRepository.planDoneSoundEffectsSwitch = isChecked
                    SettingInfoType.PLAN_RESET_SOUND_EFFECTS -> SettingMMKVRepository.planResetSoundEffectsSwitch = isChecked
                    SettingInfoType.PLAN_DELETE_SOUND_EFFECTS -> SettingMMKVRepository.planDeleteSoundEffectsSwitch = isChecked
                }
            }
        })
        binding.soundEffectsRecyclerView.apply {
            adapter = soundEffectsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}