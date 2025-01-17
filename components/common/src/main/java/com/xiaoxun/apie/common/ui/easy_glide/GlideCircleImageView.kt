package com.xiaoxun.apie.common.ui.easy_glide

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class GlideCircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    /**
     * Sets the image from a URL using Glide with a circular crop.
     */
    fun loadImage(url: String, cornerRadius: Int = 0) {
        Glide.with(context)
            .load(url)
            .apply(
                if (cornerRadius > 0) {
                    RequestOptions.bitmapTransform(RoundedCorners(cornerRadius))
                } else {
                    RequestOptions.circleCropTransform()
                }
            )
            .into(this)
    }

    /**
     * Sets the image from a drawable resource using Glide with a circular crop.
     */
    fun loadImage(resourceId: Int, cornerRadius: Int = 0) {
        Glide.with(context)
            .load(resourceId)
            .apply(
                if (cornerRadius > 0) {
                    RequestOptions.bitmapTransform(RoundedCorners(cornerRadius))
                } else {
                    RequestOptions.circleCropTransform()
                }
            )
            .into(this)
    }
}