package com.money.moa.common.util

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.io.Encoders
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class AES256 {

    lateinit var iv: String
    lateinit var keySpec: Key

    /**
     * 16자리의 키값을 입력하여 객체를 생성한다.
     *
     * @param key 암/복호화를 위한 키값
     * @throws UnsupportedEncodingException 키값의 길이가 16이하일 경우 발생
     */
    fun init(key: String) {
        iv = key.substring(0, 16)
        var keyByteArray = ByteArray(16)
        val b = key.toByteArray()
        var size: Int = b.size
        if (size > keyByteArray.size) {
            size = keyByteArray.size
        }

        keyByteArray = b.copyOf(size)
        keySpec = SecretKeySpec(keyByteArray, "AES")
    }

    /**
     * AES256 암호화
     *
     * @param str 암호화할 문자열
     * @return 암호화된 문자열
     */
    fun encrypt(str: String): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray()))
        val encrypted = c.doFinal(str.toByteArray())
        return Encoders.BASE64.encode(encrypted)
    }

    /**
     * AES256 복호화
     *
     * @param str 복호화할 문자열
     * @return 복호화된 문자열
     */
    fun decrypt(str: String): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray()))
        val byteStr = Decoders.BASE64.decode(str)
        val byteArray = c.doFinal(byteStr)
        return String(byteArray)
    }

}