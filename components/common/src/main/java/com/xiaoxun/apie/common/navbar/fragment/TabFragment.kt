package com.xiaoxun.apie.common.navbar.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class TabFragment : Fragment() {
    companion object {
        const val CONTENT: String = "content"
    }

    private var mTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mTextView = TextView(activity)
        mTextView?.gravity = Gravity.CENTER
        val content = requireArguments().getString(CONTENT)
        mTextView?.text = content
        return mTextView
    }
}