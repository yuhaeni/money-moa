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
    companion object {
        private val logger = LoggerFactory.getLogger(javaClass)
        const val FMT_10 = "yyyy-MM-dd"

        fun checkBigNumber(number: BigInteger, checkSize: Int): Boolean {
            return try {
                return number.toString().length <= checkSize
            } catch (e: NumberFormatException) {
                false
            } catch (e: Exception) {
                logger.error("", e)
                false
            }
        }
    }


}
