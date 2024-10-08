package com.money.moa.tool

import com.money.moa.common.error.enums.CommonErrorCode
import com.money.moa.common.exception.CommonException
import com.money.moa.common.properties.FileProperties
import com.money.moa.common.tool.FileTool
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc

class FileToolTest {

    private lateinit var fileTool: FileTool
    private lateinit var fileProperties: FileProperties
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp() {
        fileProperties = FileProperties("/Users/haeni/Documents/upload/")
        fileTool = FileTool(fileProperties)
    }

    @Test
    @DisplayName("파일 업로드 테스트")
    fun uploadFile() {
        // given
        val mockFile = MockMultipartFile(
                "file",
                "testFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Test file content".toByteArray()
        )
        val saveFileName = fileTool.getSaveFileName(mockFile.originalFilename)

        // when
        try {
            fileTool.uploadFile(mockFile, saveFileName)
        } catch (e: Exception) {
            throw CommonException(CommonErrorCode.INTERNAL_SERVER_ERROR)
        }

        // then
    }
}