package com.xiaoxun.apie.common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.internal.ContextUtils
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.utils.show

class APieLoadingDialog(context: Context, enableDelayShow: Boolean = true) :
    AppCompatDialog(context, R.style.APieLoadingDialog) {

    private val loadingShowDelay by lazy {
        50L
    }

    @SuppressLint("RestrictedApi", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.apie_loading_dialog)
        setCancelable(false)
//        val containerView: LottieAnimationView? = findViewById(R.id.redLoadingDialogView)
        val loadingView: APieLoadingView? = findViewById(R.id.loadingView)
        loadingView?.show()
//        if (loadingShowDelay > 0) {
//            containerView?.postDelayed({
//                //不能用isShowing，可能还未完全显示，返回false导致无法显示
//                if (ContextUtils.getActivity(context)?.isFinishing != true && ContextUtils.getActivity(
//                        context
//                    )?.isDestroyed != true
//                ) {
//                    //containerView.show()
//                }
//            }, loadingShowDelay)
//        } else {
//            //containerView.show()
//        }
    }
}