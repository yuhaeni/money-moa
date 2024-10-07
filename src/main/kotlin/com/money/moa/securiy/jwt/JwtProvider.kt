package com.money.moa.securiy.jwt

import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.common.util.AES256
import com.money.moa.redis.util.RedisUtil
import com.money.moa.securiy.CustomUserDetails
import com.money.moa.securiy.jwt.properties.JwtProperties
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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
class JwtProvider @Autowired constructor(
        private val redisUtil: RedisUtil,
        private val aeS256: AES256,
        private val jwtProperties: JwtProperties
) {
    private var DECODE_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.secretKey))
    private val logger = LoggerFactory.getLogger(javaClass)

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
        val accessCalendar = Calendar.getInstance()
        val refreshCalendar = Calendar.getInstance()
        accessCalendar.timeInMillis = accessCalendar.timeInMillis + Integer.parseInt(jwtProperties.access.expireMilliseconds, 10);
        refreshCalendar.timeInMillis = refreshCalendar.timeInMillis + Integer.parseInt(jwtProperties.refresh.expireMilliseconds, 10);

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
            response.setHeader(jwtProperties.access.tokenHeaderName, encryptToken(token))
        } catch (e: Exception) {
            throw CommonException(HttpStatus.INTERNAL_SERVER_ERROR, "encrypt token fail")
        }
    }

    /**
     * refresh-token redis에 저장
     *
     * @param claims
     * @param token
     */
    fun saveRefreshTokenToRedis(claims: Claims, token: String) {
        redisUtil.setRedisValueWithTimeout(jwtProperties.refresh.tokenHeaderName.plus(":").plus(claims.subject), encryptToken(token), 540)
    }

    private fun encryptToken(token: String): String {
        aeS256.init(jwtProperties.encryptKey)
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
        try {
            extractAllClaims(token)
        } catch (e: ExpiredJwtException) {
            logger.error("", e)
            throw CommonException("Expired Jwt")
        } catch (e: Exception) {
            logger.error("", e)
            throw CommonException(HttpStatus.INTERNAL_SERVER_ERROR, e.message.toString())
        }

        return true
    }

    /**
     * 블랙리스트 토큰인지 확인
     *
     * @param key
     * @return 블랙리스트 토큰이면 true
     */
    fun isBlackListToken(subject: String): Boolean {
        val value = redisUtil.getRedisValue(jwtProperties.refresh.tokenHeaderName.plus(":").plus(subject))
        return (value.isBlank() || value == "LOGOUT")
    }

    /**
     * 헤더에서 access-token 추출
     *
     * @param request
     * @return access-token
     */
    fun resolveAccessTokenInHeader(request: HttpServletRequest): String {
        return request.getHeader(jwtProperties.access.tokenHeaderName).orEmpty()
    }

    /**
     * reids에서 refresh-token 추출
     *
     * @param request
     * @return access-token
     */
    fun resolveRefreshTokenInRedis(subject: String): String {
        return redisUtil.getRedisValue(jwtProperties.refresh.tokenHeaderName.plus(":").plus(subject))
    }

    /**
     * 토큰 복호화
     *
     * @param encryptToken
     * @return 복호화된 token
     */
    fun decryptToken(encryptToken: String): String {
        try {
            jwtProperties.encryptKey.let { aeS256.init(it) }
            return aeS256.decrypt(encryptToken)
        } catch (e: Exception) {
            logger.error("", e)
            throw CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR)
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

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    /**
     * Claims(payload) 에서 UserDetails 추출
     *
     * @param claims
     * @return UserDetails
     */
    fun getUserDetails(claims: Claims): UserDetails {
        val authorities = ArrayList<GrantedAuthority>()
        if (claims.containsKey("authorities")) {
            for (authority in (claims["authorities"] as ArrayList<String>)) {
                authorities.add(SimpleGrantedAuthority(authority))
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
        response.setHeader(jwtProperties.access.tokenHeaderName, "")
    }

    /**
     * redis에 블랙리스트 토큰 등록
     *
     * @param request
     */
    fun setBlackListToken(httpServletRequest: HttpServletRequest) {
        var encryptAccessToken = this.resolveAccessTokenInHeader(httpServletRequest)
        if (encryptAccessToken.isNotBlank()) {
            encryptAccessToken = encryptAccessToken.replace("Bearer ", "")
            val accessToken = this.decryptToken(encryptAccessToken)
            val jwtClaims = extractAllClaims(accessToken)
            redisUtil.modifyRedisValue(jwtProperties.refresh.tokenHeaderName.plus(":").plus(jwtClaims.payload.subject), "LOGOUT")
        }
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
                StringUtils.isNotBlank(encryptAccessToken)
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
                logger.error("", e)
                val encryptRefreshToken = redisUtil.getRedisValue(jwtProperties.refresh.tokenHeaderName.plus(":").plus(e.claims.subject))
                if (StringUtils.isNotBlank(encryptRefreshToken)) {
                    val refreshToken = decryptToken(encryptRefreshToken)
                    if (
                            StringUtils.equals(refreshToken, accessToken)
                            && validateToken(refreshToken)
                    ) {
                        issueToken(response, e.claims)
                    }
                }
            } catch (e: Exception) {
                logger.error("", e)
                removeAuthentication(request, response)
                e.printStackTrace()
                throw Exception(e)
            }
        }

    }
}