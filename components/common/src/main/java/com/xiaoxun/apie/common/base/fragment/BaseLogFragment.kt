package com.xiaoxun.apie.common.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xiaoxun.apie.common.utils.APieLog

/**
 * 生命周期log打印
 */
abstract class BaseLogFragment: Fragment() {
    companion object {
        private const val TAG = "BaseLogFragment"
    }

    protected var className: String = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        APieLog.d(TAG, "$className --> onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        APieLog.d(TAG, "$className --> onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APieLog.d(TAG, "$className --> onViewCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        APieLog.d(TAG, "$className --> onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
        APieLog.d(TAG, "$className --> onStart")
    }

    override fun onResume() {
        super.onResume()
        APieLog.d(TAG, "$className --> onResume")
    }

    override fun onPause() {
        super.onPause()
        APieLog.d(TAG, "$className --> onPause")
    }

    override fun onStop() {
        super.onStop()
        APieLog.d(TAG, "$className --> onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        APieLog.d(TAG, "$className --> onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        APieLog.d(TAG, "$className --> onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        APieLog.d(TAG, "$className --> onDestroy")
    }
}