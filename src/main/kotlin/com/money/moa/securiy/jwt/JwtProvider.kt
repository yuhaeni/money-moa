package com.money.moa.securiy.jwt

import com.money.moa.redis.util.RedisUtil
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider(
        private val redisUtil: RedisUtil,
        @Value("\${jwt.secret-key}")
        var SECRET_KEY: String,

        @Value("\${jwt.encrypt-key}")
        var ENCRYPT_KEY: String,

        @Value("\${jwt.access.expire-milliseconds}")
        var ACCESS_EXPIRE_MILLISECONDS: String,

        @Value("\${jwt.refresh.expire-milliseconds}")
        var REFRESH_EXPIRE_MILLISECONDS: String,

        @Value("\${jwt.access.token-header-name}")
        var ACCESS_TOKEN_HEADER_NAME: String,

        @Value("\${jwt.refresh.token-header-name}")
        var REFRESH_TOKEN_HEADER_NAME: String,
) {
    // 출처: https://velog.io/@kimjiwonpg98/javakotlin-jwt-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0

    private var DECODE_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))

    /**
     * 토큰 발급
     *
     * @param response
     * @param claims
     */
    fun issueToken(response: HttpServletResponse, claims: Claims) {
        val jwtDto = getTokenDto(response, claims)
        saveAccessTokenToHeader(response, jwtDto.accessToken)
        saveRefreshTokenToRedis(claims, jwtDto.refreshToken)
    }


    /**
     * 토큰 Claims 호출
     *
     * @param token
     * @return 서명된 JWT
     */
    private fun extractAllClaims(token: String): Jws<Claims> {
        return Jwts.parser()
                .verifyWith(DECODE_SECRET_KEY)
                .build()
                .parseSignedClaims(token)
    }

    /**
     * 토큰 dto 생성
     *
     * @param token
     * @param claims
     * @return JwtDto
     */
    fun getTokenDto(response: HttpServletResponse, claims: Claims): JwtDto {

        val accessCalendar = Calendar.getInstance()
        val refreshCalendar = Calendar.getInstance()
        accessCalendar.timeInMillis = accessCalendar.timeInMillis + Integer.parseInt(ACCESS_EXPIRE_MILLISECONDS, 10);
        refreshCalendar.timeInMillis = refreshCalendar.timeInMillis + Integer.parseInt(REFRESH_EXPIRE_MILLISECONDS, 10);

        val accessDate = Date.from(accessCalendar.toInstant())
        val refreshDate = Date.from(refreshCalendar.toInstant())

        val issueDate = Date()
        val accessToken = generateToken(claims, issueDate, accessDate)
        val refreshToken = generateToken(claims, issueDate, refreshDate)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return JwtDto(
                accessToken = accessToken,
                accessTokenExpirationDate = accessDate,
                accessTokenExpirationDateStr = sdf.format(accessDate),
                refreshToken = refreshToken,
                refreshTokenExpirationDate = refreshDate,
                refreshTokenExpirationDateStr = sdf.format(refreshDate)
        )
    }

    fun generateToken(claims: Claims, now: Date, expirationDate: Date): String {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(DECODE_SECRET_KEY)
                .compact()
    }

    /**
     * access-token header에 저장
     *
     * @param claims
     * @param token
     */
    fun saveAccessTokenToHeader(response: HttpServletResponse, token: String) {
        response.setHeader(ACCESS_TOKEN_HEADER_NAME, encryptToken(token))
    }

    /**
     * refresh-token redis에 저장
     *
     * @param claims
     * @param token
     */
    fun saveRefreshTokenToRedis(claims: Claims, token: String) {
        redisUtil.setRedisValueWithTimeout(REFRESH_TOKEN_HEADER_NAME.plus(":").plus(claims.subject), encryptToken(token), REFRESH_EXPIRE_MILLISECONDS.toLong())
    }

    private fun encryptToken(token: String): String {
        val keySpec = SecretKeySpec(ENCRYPT_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val ciphertext = cipher.doFinal(token.toByteArray())
        val encodedByte = Encoders.BASE64.encode(ciphertext)

        return encodedByte.toString()
    }

    /**
     * build 클레임
     *
     * @param memberSeq
     * @param authorities
     * @return JWT 클레임
     */
    fun buildClaims(email: String, authorities: Set<String>): Claims {
        return Jwts.claims().subject(email)
                .add("authorities", authorities)
                .build()
    }
}