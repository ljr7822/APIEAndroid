package com.xiaoxun.apie.common.navbar

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView

/**
 * 去除LottieAnimationView的缓存
 */
class APieLottieAnimationView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LottieAnimationView(context, attrs, defStyleAttr) {

    /**
     * 重写此方法将LottieAnimationView的缓存去除
     * 解决因异常情况或旋转方向后页面重新加载
     * 导致lottie文件读取成最后一个tab文件的bug
     */
    override fun onSaveInstanceState(): Parcelable? {
        var parcelable = super.onSaveInstanceState()
        parcelable = null
        return null
    }
}