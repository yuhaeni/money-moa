package com.money.moa.category.controller

import com.money.moa.category.dto.response.CategoryFindResponse
import com.money.moa.category.dto.request.CategorySaveRequest
import com.money.moa.category.service.CategoryService
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
        categoryService.saveCategory(categorySaveRequest)
    }

    @GetMapping
    fun findAllCategory(): List<CategoryFindResponse> {
        return categoryService.findAllCategory()
    }
}