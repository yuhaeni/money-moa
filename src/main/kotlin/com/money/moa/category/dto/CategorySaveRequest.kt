package com.money.moa.category.dto

import com.money.moa.category.domain.Category

class CategorySaveRequest(
        var categoryName: String,
        var categoryType: Char
) {
    fun toEntity(): Category {
        return Category(
                categoryName = categoryName,
                categoryType = categoryType
        )
    }
}