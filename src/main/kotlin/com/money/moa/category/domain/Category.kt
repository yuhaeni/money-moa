package com.money.moa.category.domain

import com.money.moa.category.dto.response.CategoryFindResponse
import com.money.moa.category.dto.response.CategorySaveResponse
import com.money.moa.common.domain.BaseEntity
import jakarta.persistence.*
import lombok.Getter

@Table
@Entity
@Getter
class Category(
        @Column
        val categoryName: String,
        @Column
        val categoryType: String,
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val categoryId: Long? = null

        // TODO 아이콘 이미지 연관관계 추가
) : BaseEntity() {
    fun fromEntity(): CategoryFindResponse {
        return CategoryFindResponse(
                categoryName = categoryName,
                categoryType = categoryType
        )
    }

    fun toDto(): CategorySaveResponse {
        return CategorySaveResponse(
                categoryName = categoryName,
                categoryType = categoryType
        )
    }
}