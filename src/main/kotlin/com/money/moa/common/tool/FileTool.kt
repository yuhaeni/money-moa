package com.money.moa.common.tool

import com.money.moa.common.properties.FileProperties
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

class FileTool(private val fileProperties: FileProperties) {
    private val UPLOAD_DIR = fileProperties.uploadDir

    /**
     * 파일 업로드
     *
     * @param file
     */
    fun uploadFile(file: MultipartFile) {
        val saveFileName = getSaveFileName(file.originalFilename as String)
        file.transferTo(File(UPLOAD_DIR + saveFileName))
    }

    private fun getSaveFileName(originalFilename: String): String {
        val extPostIndex: Int = originalFilename.lastIndexOf(".")
        val ext = originalFilename.substring(extPostIndex.plus(1))

        return UUID.randomUUID().toString().plus(".").plus(ext)
    }

}