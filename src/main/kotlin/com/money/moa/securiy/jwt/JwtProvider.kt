package com.money.moa.securiy.jwt

import com.money.moa.common.util.AES256
import com.money.moa.redis.util.RedisUtil
import com.money.moa.securiy.CustomUserDetails
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Component
class JwtProvider(
        private val redisUtil: RedisUtil,
        private val aeS256: AES256,
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

        val authentication = getAuthentication(jwtDto.refreshToken)
        SecurityContextHolder.getContext().authentication = authentication
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

        // TODO DateTool 추가
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
        try {
            response.setHeader(ACCESS_TOKEN_HEADER_NAME, encryptToken(token))
        } catch (e: Exception) {
            throw IllegalArgumentException("encrypt token fail")
        }
    }

    /**
     * refresh-token redis에 저장
     *
     * @param claims
     * @param token
     */
    fun saveRefreshTokenToRedis(claims: Claims, token: String) {
        encryptToken(token)?.let { redisUtil.setRedisValueWithTimeout(REFRESH_TOKEN_HEADER_NAME.plus(":").plus(claims.subject), it, REFRESH_EXPIRE_MILLISECONDS.toLong()) }
    }

    private fun encryptToken(token: String): String? {
        aeS256.init(ENCRYPT_KEY)
        return aeS256.encrypt(token)
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


    /**
     * 토큰 검증
     *
     * @param token
     * @return 유효한 토큰이면 true
     */
    fun validateToken(token: String): Boolean {
        if (isBlackListToken(token)) {
            return false
        }

        try {
            extractAllClaims(token)
        } catch (e: ExpiredJwtException) {
            throw e
        }

        return true
    }

    /**
     * 블랙리스트 토큰인지 확인
     *
     * @param token
     * @return 블랙리스트 토큰이면 true
     */
    fun isBlackListToken(token: String): Boolean {
        val value = redisUtil.getRedisValue(token)
        return (value.isNotBlank() && value == "LOGOUT")
    }

    /**
     * 헤더에서 access-token 추출
     *
     * @param request
     * @return access-token
     */
    fun resolveAccessTokenInHeader(request: HttpServletRequest): String {
        return request.getHeader(ACCESS_TOKEN_HEADER_NAME).orEmpty()
    }

    /**
     * reids에서 refresh-token 추출
     *
     * @param request
     * @return access-token
     */
    fun resolveRefreshTokenInRedis(subject: String): String {
        return redisUtil.getRedisValue(subject)
    }

    /**
     * 토큰 복호화
     *
     * @param encryptToken
     * @return 복호화된 token
     */
    fun decryptToken(encryptToken: String): String {
        // TODO 수정 필요

        println(encryptToken)
        try {
            aeS256.init(ENCRYPT_KEY)
            return aeS256.decrypt(encryptToken)
        } catch (e: Exception) {
            throw IllegalArgumentException("decrypt token fail")
        }
    }

    /**
     * 스프링 시큐리티 검증
     *
     * @param token
     * @return 복호화된 token
     */
    fun getAuthentication(token: String): Authentication {
        val jwtClaims = extractAllClaims(token)
        val userDetails = getUserDetails(jwtClaims.payload)

        return UsernamePasswordAuthenticationToken(userDetails, userDetails.authorities)
    }

    /**
     * Claims 에서 UserDetails 추출
     *
     * @param claims
     * @return UserDetails
     */
    fun getUserDetails(claims: Claims): UserDetails {
        val authorities = ArrayList<GrantedAuthority>()
        if (claims.containsKey("authorities")) {
            val authoritiesList = listOf(claims["authorities"])
            for (authority in authoritiesList) {
                authorities.add(SimpleGrantedAuthority(authority.toString()))
            }
        }

        return CustomUserDetails(
                userName = claims.subject,
                userPassword = "",
                authorities = authorities
        )
    }

    /**
     * 토큰 제거
     *
     * @param request
     * @param response
     */
    fun destroyToken(request: HttpServletRequest, response: HttpServletResponse, subject: String) {
        removeAuthentication(request, response)
        redisUtil.removeRedisValue(subject)
    }

    /**
     * 인증 제거
     *
     * @param request
     * @param response
     */
    fun removeAuthentication(request: HttpServletRequest, response: HttpServletResponse) {
        SecurityContextHolder.clearContext()
        request.session.invalidate()
        response.setHeader(ACCESS_TOKEN_HEADER_NAME, "")
    }

    /**
     * redis에 블랙리스트 토큰 등록
     *
     * @param request
     */
    fun setBlackListToken(token: String) {
        val jwtClaims = extractAllClaims(token)
        redisUtil.modifyRedisValue(REFRESH_TOKEN_HEADER_NAME.plus(":").plus(jwtClaims.payload), "LOGOUT")
    }

    /**
     * 필터에서 jwt 인증 유효성 확인
     *
     * @param request
     * @param response
     */
    fun filterValidator(request: HttpServletRequest, response: HttpServletResponse) {
        var encryptAccessToken = resolveAccessTokenInHeader(request)
        if (
                encryptAccessToken.isNotBlank()
                && encryptAccessToken.startsWith("Bearer ")
        ) {
            encryptAccessToken = encryptAccessToken.replace("Bearer ", "").trim()
            val accessToken = decryptToken(encryptAccessToken)

            try {
                if (validateToken(accessToken)) {
                    val authentication = getAuthentication(accessToken)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (e: ExpiredJwtException) {
                removeAuthentication(request, response)
            } catch (e: Exception) {
                removeAuthentication(request, response)
                e.printStackTrace()
                throw Exception(e)
            }
        }

    }
}