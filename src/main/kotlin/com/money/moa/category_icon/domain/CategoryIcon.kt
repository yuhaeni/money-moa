package com.money.moa.category_icon.domain

import com.money.moa.category_icon.dto.response.CategoryIconSaveResponse
import com.money.moa.common.domain.BaseEntity
import jakarta.persistence.*

@Table
@Entity
class CategoryIcon(
        @Column
        var saveFileName: String,

        @Column
        var originalFileName: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val categoryIconId: Long? = null
) : BaseEntity() {
    fun toDto(): CategoryIconSaveResponse {
        return CategoryIconSaveResponse(
                saveFileName = saveFileName,
                originalFileName = originalFileName,
        )
    }
}