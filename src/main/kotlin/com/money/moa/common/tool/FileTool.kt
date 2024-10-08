package com.money.moa.common.tool

import com.money.moa.common.properties.FileProperties
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@Component
class FileTool(private val fileProperties: FileProperties) {
    private val UPLOAD_DIR = fileProperties.uploadDir

    /**
     * 파일 업로드
     *
     * @param file
     */
    fun uploadFile(file: MultipartFile, saveFileName: String) {
        file.transferTo(File(UPLOAD_DIR + saveFileName))
    }

    /**
     * 확장자 추출
     *
     * @param originalFilename
     */
    fun getExtension(originalFilename: String): String {
        val extPostIndex: Int = originalFilename.lastIndexOf(".")
        return originalFilename.substring(extPostIndex.plus(1))
    }

    /**
     * 파일명 생성
     *
     * @param originalFilename
     */
    fun getSaveFileName(originalFilename: String): String {
        val ext = getExtension(originalFilename)
        return UUID.randomUUID().toString().plus(".").plus(ext)
    }
}