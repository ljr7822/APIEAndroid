package com.xiaoxun.apie.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.SwitchCompat
import com.xiaoxun.apie.common.R

/**
 * 带loading态的设置开关
 */
class APieSwitch@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwitchCompat(context, attrs) {

    private var loadingDialog: APieLoadingDialog? = null

    private var enableLoadingState = false

    private var onClickListener: OnClickListener? = null

    init {
        enableLoadingState =
            context.obtainStyledAttributes(attrs, R.styleable.APieSwitch).getBoolean(R.styleable.APieSwitch_enableLoadingState, false)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (enableLoadingState) {
            if(ev?.action == MotionEvent.ACTION_UP) {
                //补充滑动监听
                processSwitchClick()
                return true
            } else if(ev?.action == MotionEvent.ACTION_MOVE){
                //避免滑动切换开关状态绕过loading
                return false
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        onClickListener = l
    }

    private fun processSwitchClick() {
        if (loadingDialog?.isShowing == true || isClickable.not()) return
        setLoadingState(true)
        onClickListener?.onClick(this)
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        setLoadingState(false)
    }

    fun setLoadingState(loading: Boolean) {
        alpha = if (loading || isClickable.not()) 0.4f else 1f
        if (loading) {
            if (loadingDialog == null) loadingDialog = APieLoadingDialog(context)
            loadingDialog?.show()
        } else {
            dismissLoadingDialog()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    fun setEnableLoadingState(enable: Boolean) {
        this.enableLoadingState = enable
    }

    override fun setClickable(clickable: Boolean) {
        super.setClickable(clickable)
        alpha = if (clickable.not()) 0.4f else 1f
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dismissLoadingDialog()
    }
}