package com.money.moa.category_icon.service

import com.money.moa.category.domain.CategoryRepository
import com.money.moa.category_icon.domain.CategoryIcon
import com.money.moa.category_icon.domain.CategoryIconRepository
import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.common.tool.FileTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CategoryIconService @Autowired constructor(
        private val fileTool: FileTool,
        private val categoryIconRepository: CategoryIconRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun saveCategoryIcon(file: MultipartFile) {
        try {
            val saveFileName = file.originalFilename?.let { fileTool.getSaveFileName(it) }
            if (saveFileName == null) {
                throw CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR)
            }

            fileTool.uploadFile(file, saveFileName)

            val categoryIcon = CategoryIcon(
                    originalFileName = file.originalFilename!!,
                    saveFileName = saveFileName
            )
            categoryIconRepository.save(categoryIcon)
        } catch (e: Exception) {
            logger.error(e.message)
            throw CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR)
        }
    }
}