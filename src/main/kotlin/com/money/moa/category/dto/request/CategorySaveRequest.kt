package com.money.moa.category.dto.request

import com.money.moa.category.domain.Category

class CategorySaveRequest(
        var categoryName: String,
        var categoryType: String
) {
    fun toEntity(): Category {
        return Category(
                categoryName = categoryName,
                categoryType = categoryType
        )
    }
}