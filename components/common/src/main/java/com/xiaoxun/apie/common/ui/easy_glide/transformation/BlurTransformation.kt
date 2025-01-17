package com.xiaoxun.apie.common.ui.easy_glide.transformation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.util.Util
import com.xiaoxun.apie.common.ui.easy_glide.BlurUtils
import java.security.MessageDigest


class BlurTransformation @JvmOverloads constructor(
    private val context: Context,
    radius: Int = MAX_RADIUS,
    sampling: Int = DEFAULT_SAMPLING
) : BitmapTransformation() {
    private val ID = javaClass.name
    private val radius //模糊半径0～25
            : Int = if (radius > MAX_RADIUS) MAX_RADIUS else radius
    private val sampling //取样0～25
            : Int = if (sampling > MAX_RADIUS) MAX_RADIUS else sampling

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling
        var bitmap = pool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(bitmap)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        bitmap = BlurUtils.rsBlur(context, bitmap, radius)
        return bitmap
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is BlurTransformation) {
            return radius == obj.radius && sampling == obj.sampling
        }
        return false
    }

    override fun hashCode(): Int {
        return Util.hashCode(ID.hashCode(), Util.hashCode(radius, Util.hashCode(sampling)))
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + radius * 10 + sampling).toByteArray(Key.CHARSET))
    }

    companion object {
        private const val MAX_RADIUS = 25
        private const val DEFAULT_SAMPLING = 1
    }

}

//class BlurTransformation @JvmOverloads constructor(
//    private val context: Context,
//    radius: Int = MAX_RADIUS,
//    sampling: Int = DEFAULT_SAMPLING
//) : BitmapTransformation() {
//    private val ID = javaClass.name
//    private val radius: Int = if (radius > MAX_RADIUS) MAX_RADIUS else radius
//    private val sampling: Int = if (sampling > MAX_RADIUS) MAX_RADIUS else sampling
//
//    override fun transform(
//        pool: BitmapPool,
//        toTransform: Bitmap,
//        outWidth: Int,
//        outHeight: Int
//    ): Bitmap {
//        val width = toTransform.width
//        val height = toTransform.height
//
//        // 计算居中裁剪的目标宽度和高度
//        val targetWidth = Math.min(outWidth, width)
//        val targetHeight = Math.min(outHeight, height)
//
//        // 计算裁剪区域的起始坐标，确保裁剪区域居中
//        val cropWidth = Math.min(targetWidth, width)
//        val cropHeight = Math.min(targetHeight, height)
//        val left = (width - cropWidth) / 2
//        val top = (height - cropHeight) / 2
//
//        // 创建裁剪后的位图
//        val croppedBitmap = Bitmap.createBitmap(toTransform, left, top, cropWidth, cropHeight)
//
//        // 对裁剪后的图像进行模糊处理
//        val scaledWidth = cropWidth / sampling
//        val scaledHeight = cropHeight / sampling
//        var bitmap = pool[scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888]
//
//        val canvas = Canvas(bitmap)
//        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
//        val paint = Paint()
//        paint.flags = Paint.FILTER_BITMAP_FLAG
//        canvas.drawBitmap(croppedBitmap, 0f, 0f, paint)
//
//        // 应用模糊效果
//        bitmap = BlurUtils.rsBlur(context, bitmap, radius)
//        return bitmap
//    }
//
//    override fun equals(obj: Any?): Boolean {
//        if (obj is BlurTransformation) {
//            return radius == obj.radius && sampling == obj.sampling
//        }
//        return false
//    }
//
//    override fun hashCode(): Int {
//        return Util.hashCode(ID.hashCode(), Util.hashCode(radius, Util.hashCode(sampling)))
//    }
//
//    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//        messageDigest.update((ID + radius * 10 + sampling).toByteArray(Key.CHARSET))
//    }
//
//    companion object {
//        private const val MAX_RADIUS = 25
//        private const val DEFAULT_SAMPLING = 1
//    }
//}