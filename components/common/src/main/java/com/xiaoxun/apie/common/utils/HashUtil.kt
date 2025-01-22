package com.xiaoxun.apie.common.utils

import android.content.Context
import android.net.Uri
import java.io.InputStream
import java.security.MessageDigest

/**
 * 对文件或者字符串进行Hash算法，返回MD5值
 */
object HashUtil {

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f')

    private fun convertToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (byte in bytes) {
            sb.append(HEX_DIGITS[(byte.toInt() and 0xF0) ushr 4])
            sb.append(HEX_DIGITS[byte.toInt() and 0x0F])
        }
        return sb.toString()
    }

    fun getMD5FromUri(context: Context, uri: Uri): String? {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                val buffer = ByteArray(1024)
                var numRead: Int
                while (stream.read(buffer).also { numRead = it } > 0) {
                    md5.update(buffer, 0, numRead)
                }
            }
            convertToHexString(md5.digest())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}