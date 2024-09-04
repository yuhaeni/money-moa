package com.money.moa.category.domain

import com.money.moa.category.dto.CategoryFindResponse
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
        val categoryType: Char,
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val categoryId: Long? = null
) : BaseEntity() {
        fun fromEntity(): CategoryFindResponse {
                return CategoryFindResponse(
                        categoryName = categoryName,
                        categoryType =  categoryType
                )
        }
}