package com.money.moa.category_icon.controller

import com.money.moa.category_icon.dto.response.CategoryIconSaveResponse
import com.money.moa.category_icon.service.CategoryIconService
import com.money.moa.common.dto.ResponseDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(value = ["/api/v1/category_icon"])
class CategoryIconController(
        private val categoryIconService: CategoryIconService
) {

    @PostMapping
    fun saveCategoryIcon(@RequestParam("file") file: MultipartFile): ResponseEntity<ResponseDto<CategoryIconSaveResponse>> {
        return categoryIconService.saveCategoryIcon(file)
    }

}