package com.xiaoxun.apie.common.utils.rsa

import android.util.Base64
import com.xiaoxun.apie.common.utils.APieLog
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class PasswordEncryptorUtils {
    companion object {
        private const val TAG = "PasswordEncryptorUtils"

        // 使用RSA公钥加密密码
        fun encrypt(password: String, publicKeyString: String): String {
            try {
                // 将Base64编码的公钥转换为PublicKey对象
                val publicKeyBytes = Base64.decode(publicKeyString, Base64.DEFAULT)
                val keySpec = X509EncodedKeySpec(publicKeyBytes)
                val keyFactory = KeyFactory.getInstance("RSA")
                val publicKey = keyFactory.generatePublic(keySpec)

                // 使用RSA加密密码
                val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
                cipher.init(Cipher.ENCRYPT_MODE, publicKey)
                val encryptedBytes = cipher.doFinal(password.toByteArray())

                // 返回Base64编码的加密结果
                val encryptedString = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
                APieLog.d(TAG, "进行密码加密：原始密码=$password, 加密结果=$encryptedString")
                return encryptedString
            } catch (e: Exception) {
                throw RuntimeException("加密失败: ${e.message}", e)
            }
        }
    }
}