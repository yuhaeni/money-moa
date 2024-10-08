package com.money.moa.category_icon.service

import com.money.moa.category_icon.domain.CategoryIcon
import com.money.moa.category_icon.domain.CategoryIconRepository
import com.money.moa.category_icon.dto.response.CategoryIconSaveResponse
import com.money.moa.common.dto.ResponseDto
import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.common.properties.FileProperties
import com.money.moa.common.tool.FileTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CategoryIconService @Autowired constructor(
        private val fileTool: FileTool,
        private val categoryIconRepository: CategoryIconRepository,
        private val fileProperties: FileProperties
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val UPLOAD_DIR = fileProperties.uploadDir

    @Transactional
    fun saveCategoryIcon(file: MultipartFile): ResponseEntity<ResponseDto<CategoryIconSaveResponse>> {

        val saveFileName = file.originalFilename?.let { fileTool.getSaveFileName(it) }
        if (saveFileName == null) {
            throw CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR)
        }

        try {
            fileTool.uploadFile(file, saveFileName)
        } catch (e: Exception) {
            logger.error(e.message)
            throw CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR)
        }

        val categoryIcon = CategoryIcon(
                originalFileName = file.originalFilename!!,
                saveFileName = saveFileName,
                filePath = UPLOAD_DIR.plus(saveFileName)
        )
        val saveCategoryIcon = categoryIconRepository.save(categoryIcon)
        return ResponseDto.created(saveCategoryIcon.toDto())
    }

    fun findCategoryIcon() {
        // TODO 아이콘 조회 기능 구현
    }
}