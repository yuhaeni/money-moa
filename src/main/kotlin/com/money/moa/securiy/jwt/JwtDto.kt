package com.money.moa.securiy.jwt

import java.util.Date

data class JwtDto(
        var accessToken: String,
        var accessTokenExpirationDateStr: String,
        var accessTokenExpirationDate: Date,
        var refreshToken: String,
        var refreshTokenExpirationDateStr: String,
        var refreshTokenExpirationDate: Date
)