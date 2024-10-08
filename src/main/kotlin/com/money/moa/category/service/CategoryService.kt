package com.money.moa.category.service

import com.money.moa.category.domain.Category
import com.money.moa.category.domain.CategoryRepository
import com.money.moa.category.dto.response.CategoryFindResponse
import com.money.moa.category.dto.request.CategorySaveRequest
import com.money.moa.category.dto.response.CategorySaveResponse
import com.money.moa.common.dto.ResponseDto
import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.common.manager.CategoryManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CategoryService @Autowired constructor(
        private val categoryRepository: CategoryRepository,
        private val categoryManager: CategoryManager
) {
    fun saveCategory(categorySaveRequest: CategorySaveRequest): ResponseEntity<ResponseDto<CategorySaveResponse>> {
        validateCategory(categorySaveRequest)

        // TODO 아이콘 이미지 저장

        val category = categoryRepository.save(categorySaveRequest.toEntity())
        return ResponseDto.created(category.toDto())
    }

    private fun validateCategory(categorySaveRequest: CategorySaveRequest) {
        // TODO 유효성 검사 추가
        if (!categoryManager.checkCategoryType(categorySaveRequest.categoryType)) {
            throw CommonException(CommonErrorCode.INVALID_PARAMETER)
        }
    }

    fun findAllCategory(): List<CategoryFindResponse> {
        val categoryList: List<Category> = categoryRepository.findAll()
        return categoryList.map { it.fromEntity() }.toList()
    }

}