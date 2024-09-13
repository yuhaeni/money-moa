package com.money.moa.category.controller

import com.money.moa.category.dto.CategoryFindResponse
import com.money.moa.category.dto.CategorySaveRequest
import com.money.moa.category.service.CategoryService
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/category"])
class CategoryController(
        private val categoryService: CategoryService
) {
    @PostMapping
    fun saveCategory(@RequestBody categorySaveRequest: CategorySaveRequest) {
       // TODO 관리자 권한만 가능
        categoryService.saveCategory(categorySaveRequest)
    }

    @GetMapping
    fun findAllCategory(): List<CategoryFindResponse> {
        return categoryService.findAllCategory()
    }
}