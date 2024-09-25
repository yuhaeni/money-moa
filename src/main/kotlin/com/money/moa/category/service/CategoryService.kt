package com.money.moa.category.service

import com.money.moa.category.domain.Category
import com.money.moa.category.domain.CategoryRepository
import com.money.moa.category.dto.CategoryFindResponse
import com.money.moa.category.dto.CategorySaveRequest
import org.springframework.stereotype.Service

@Service
class CategoryService(
        private val categoryRepository: CategoryRepository
) {
    fun saveCategory(categorySaveRequest: CategorySaveRequest){
        // TODO 아이콘 이미지 저장
        categoryRepository.save(categorySaveRequest.toEntity())
    }

    fun findAllCategory(): List<CategoryFindResponse> {
        val categoryList: List<Category> = categoryRepository.findAll()
        return categoryList.map { it.fromEntity() }.toList()
    }

}