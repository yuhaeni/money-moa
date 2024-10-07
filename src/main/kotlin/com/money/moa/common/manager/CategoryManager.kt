package com.money.moa.common.manager

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

@Service
class CategoryManager {
    fun checkCategoryType(categoryType: String): Boolean {
        return StringUtils.equals(categoryType, "O")
                || StringUtils.equals(categoryType, "I")
    }
}