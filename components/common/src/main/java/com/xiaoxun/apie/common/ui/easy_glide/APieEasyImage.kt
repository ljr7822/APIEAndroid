package com.xiaoxun.apie.common.ui.easy_glide

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.util.Preconditions
import com.xiaoxun.apie.common.R
import com.xiaoxun.apie.common.ui.easy_glide.config.GlideConfigImpl
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import com.xiaoxun.apie.common.ui.easy_glide.progress.*
import com.xiaoxun.apie.common.ui.easy_glide.transformation.*

object APieEasyImage {

    var placeHolderImageView = R.color.apieTheme_colorTransparent
    var circlePlaceholderImageView = R.color.apieTheme_colorTransparent

    /**
     * 加载本地图片
     *
     * @param context
     * @param drawableId 本地图片资源id
     */

    @JvmStatic
    fun ImageView.loadImage(context: Context, @RawRes @DrawableRes drawableId: Int) {
        loadImage(
            context, GlideConfigImpl
                .builder()
                .drawableId(drawableId)
                .isCropCenter(true)
                .imageView(this)
                .build()
        )
    }

    /**
     * 加载网络图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadImage(
        context: Context,
        url: String?,
        @DrawableRes placeHolder: Int = placeHolderImageView,
        onProgressListener: OnProgressListener? = null,
        requestListener: RequestListener<Drawable?>? = null
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .isCropCenter(true)
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .progressListener(onProgressListener)
                .requestListener(requestListener)
                .build()
        )
    }

    /**
     * 加载本地图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadResizeXYImage(
        context: Context,
        url: String?,
        resizeX: Int,
        resizeY: Int,
        @DrawableRes placeHolder: Int = placeHolderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .isCropCenter(true)
                .isCrossFade(true)
                .resize(resizeX, resizeY)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }

    /**
     * 加载圆形图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadCircleImage(
        context: Context,
        url: String?,
        @DrawableRes placeHolder: Int = circlePlaceholderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .isCropCircle(true)
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }

    /**
     * 加载灰度图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadGrayImage(
        context: Context,
        url: String?,
        @DrawableRes placeHolder: Int = placeHolderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .transformation(CenterCrop(), GrayscaleTransformation())
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }

    /**
     * 加载模糊图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadBlurImage(
        context: Context,
        url: String?,
        radius: Int = 10,
        @DrawableRes placeHolder: Int = placeHolderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .transformation(CenterCrop(), BlurTransformation(context, radius))
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }

    /**
     * 加载圆角图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadRoundCornerImage(
        context: Context,
        url: String?,
        radius: Int = 20,
        margin: Int = 0,
        @DrawableRes placeHolder: Int = placeHolderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .transformation(CenterCrop(), RoundedCornersTransformation(radius, margin))
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }

    /**
     * 加载带边框的圆形图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadCircleWithBorderImage(
        context: Context,
        url: String?,
        borderWidth: Int = 2,
        @ColorInt borderColor: Int = 0xACACAC,
        @DrawableRes placeHolder: Int = placeHolderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .transformation(CircleWithBorderTransformation(borderWidth, borderColor))
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }

    /**
     * 加载带边框的图片
     */
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadBorderImage(
        context: Context,
        url: String?,
        borderWidth: Int = 2,
        @ColorInt borderColor: Int = 0xACACAC,
        @DrawableRes placeHolder: Int = placeHolderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .transformation(BorderTransformation(borderWidth, borderColor))
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }

    /**
     * 提供了一下如下变形类，支持叠加使用
     * BlurTransformation
     * GrayScaleTransformation
     * RoundedCornersTransformation
     * CircleCrop
     * CenterCrop
     */
    @JvmStatic
    fun ImageView.loadImageWithTransformation(
        context: Context,
        url: String?,
        vararg bitmapTransformations: BitmapTransformation,
        @DrawableRes placeHolder: Int = placeHolderImageView
    ) {
        loadImage(
            context,
            GlideConfigImpl
                .builder()
                .url(url)
                .transformation(*bitmapTransformations)
                .isCrossFade(true)
                .errorPic(placeHolder)
                .placeholder(placeHolder)
                .imageView(this)
                .build()
        )
    }


    /**
     * 预加载
     */
    @JvmStatic
    fun preloadImage(context: Context, url: String?) {
        Glide.with(context).load(url).preload()
    }

    @SuppressLint("CheckResult")
    fun loadImage(context: Context, config: GlideConfigImpl) {
        Preconditions.checkNotNull(context, "Context is required")
        Preconditions.checkNotNull(config, "ImageConfigImpl is required")
        Preconditions.checkNotNull(config.imageView, "ImageView is required")
        val glideRequest = if (config.drawableId != 0) {
            Glide.with(context).load(config.drawableId)
        } else {
            Glide.with(context).load(config.url)
        }
        glideRequest.apply {
            when (config.cacheStrategy) {
                0 -> diskCacheStrategy(DiskCacheStrategy.ALL)
                1 -> diskCacheStrategy(DiskCacheStrategy.NONE)
                2 -> diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                3 -> diskCacheStrategy(DiskCacheStrategy.DATA)
                4 -> diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                else -> diskCacheStrategy(DiskCacheStrategy.ALL)
            }
            if (config.isCrossFade) {
                val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                transition(DrawableTransitionOptions.withCrossFade(factory))
            }
            if (config.isCrossFade) {
                val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                transition(DrawableTransitionOptions.withCrossFade(factory))
            }
            if (config.isImageRadius()) {
                transform(RoundedCorners(config.imageRadius))
            }
            if (config.isBlurImage) {
                transform(BlurTransformation(context, config.blurValue))
            }
            //glide用它来改变图形的形状
            if (config.transformation != null) {
                transform(*config.transformation)
            }
            if (config.placeHolderDrawable != null) {
                placeholder(config.placeHolderDrawable)
            }
            //设置占位符
            if (config.placeholder != 0) {
                placeholder(config.placeholder)
            }
            //设置错误的图片
            if (config.errorPic != 0) {
                error(config.errorPic)
            }
            //设置请求 url 为空图片
            if (config.fallback != 0) {
                fallback(config.fallback)
            }
            if (config.resizeX != 0 && config.resizeY != 0) {
                override(config.resizeX, config.resizeY)
            }
            if (config.isCropCenter) {
                centerCrop()
            }
            if (config.isCropCircle) {
                circleCrop()
            }
            if (config.formatType != null) {
                format(config.formatType)
            }
            if (config.isFitCenter) {
                fitCenter()
            }
            if (config.requestListener != null) {
                addListener(config.requestListener)
            }
            into(GlideImageViewTarget(config.imageView, config.url))
        }

        if (config.onProgressListener != null && !config.url.isNullOrBlank()) {
            ProgressManager.addListener(config.url!!, config.onProgressListener)
        }


    }

    /**
     * 清除本地缓存
     */
    @SuppressLint("CheckResult")
    fun clearDiskCache(context: Context) {
        Observable.just(0)
            .observeOn(Schedulers.io())
            .subscribe { Glide.get(context).clearDiskCache() }
    }

    /**
     * 清除内存缓存
     */
    @SuppressLint("CheckResult")
    @JvmStatic
    fun clearMemory(context: Context) {
        Observable.just(0)
            .observeOn(Schedulers.io())
            .subscribe { Glide.get(context).clearDiskCache() }
    }

    /**
     * 取消图片加载
     */
    @JvmStatic
    fun clearImage(context: Context, imageView: ImageView) {
        Glide.get(context).requestManagerRetriever[context].clear(imageView)
    }
}