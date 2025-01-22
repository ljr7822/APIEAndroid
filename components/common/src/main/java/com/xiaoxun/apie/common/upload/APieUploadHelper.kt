package com.xiaoxun.apie.common.upload

import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.xiaoxun.apie.common.base.APieBaseApplication
import com.xiaoxun.apie.common.repo.AliyunMMKVRepository
import com.xiaoxun.apie.common.utils.APieLog
import com.xiaoxun.apie.common.utils.HashUtil
import java.io.File
import java.io.InputStream
import java.util.Date

/**
 * 上传工具类: 上传任意文件到阿里云oss存储
 */
object APieUploadHelper {
    private const val TAG = "APieUploadHelper"

    // 使用 STS 凭证初始化 OSS 客户端
    private val credentialProvider = OSSStsTokenCredentialProvider(
        AliyunMMKVRepository.accessKeyId,
        AliyunMMKVRepository.accessKeySecret,
        AliyunMMKVRepository.securityToken,
    )

    private fun getClient(): OSS {
        return OSSClient(
            APieBaseApplication.application(),
            AliyunMMKVRepository.endpoint,
            credentialProvider
        )
    }

    /**
     * 上传文件，支持文件路径或 Uri
     *
     * @param objKey OSS 上的存储键
     * @param uri 文件的 Uri
     * @return 上传成功后的 URL 或错误信息
     */
    private fun upload(context: Context, objKey: String, uri: Uri): String {
        try {
            // 打开输入流
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val tempFile = createTempFile(context, inputStream)
                val request =
                    PutObjectRequest(AliyunMMKVRepository.bucketName, objKey, tempFile.absolutePath)

                // 初始化上传客户端
                val client = getClient()
                client.putObject(request)

                // 生成外部访问 URL
                val url = client.presignPublicObjectURL(AliyunMMKVRepository.bucketName, objKey)
                APieLog.d(TAG, "文件上传成功，URL=$url")
                return url
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return e.toString()
        }
        return ""
    }

    /**
     * 上传普通图片
     */
    fun uploadImage(context: Context, uri: Uri): String {
        val key = getImageKey(context, uri)
        return upload(context, key, uri)
    }

    /**
     * 上传头像
     */
    fun uploadAvatar(context: Context, uri: Uri): String {
        val key = getAvatarKey(context, uri)
        return upload(context, key, uri)
    }

    /**
     * 上传音频
     */
    fun uploadAudio(context: Context, uri: Uri): String {
        val key = getAudioKey(context, uri)
        return upload(context, key, uri)
    }

    /**
     * 生成分月存储的路径
     */
    private fun getDataString(): String {
        return DateFormat.format("yyyyMM", Date()).toString()
    }

    private fun getImageKey(context: Context, uri: Uri): String {
        val fileMd5 = HashUtil.getMD5FromUri(context, uri)
        return "image/${getDataString()}/${fileMd5}.jpg"
    }

    private fun getAvatarKey(context: Context, uri: Uri): String {
        val fileMd5 = HashUtil.getMD5FromUri(context, uri)
        return "avatar/${getDataString()}/${fileMd5}.jpg"
    }

    private fun getAudioKey(context: Context, uri: Uri): String {
        val fileMd5 = HashUtil.getMD5FromUri(context, uri)
        return "audio/${getDataString()}/${fileMd5}.mp3"
    }

    /**
     * 从 InputStream 创建临时文件
     */
    private fun createTempFile(context: Context, inputStream: InputStream): File {
        val tempFile = File.createTempFile("upload_temp", null, context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return tempFile
    }
}
