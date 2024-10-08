package com.money.moa.account_log.manager

import com.money.moa.account_log.domain.AccountLogRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate

@Component
class AccountLogManager {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun checkBigNumber(number: BigInteger): Boolean {
        return try {
            return number.toString().length <= 20
        } catch (e: NumberFormatException) {
            false
        } catch (e: Exception) {
            logger.error("", e)
            false
        }
    }
}
