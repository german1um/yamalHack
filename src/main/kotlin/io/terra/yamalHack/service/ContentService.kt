package io.terra.yamalHack.service

import io.terra.yamalHack.api.UploadService
import io.terra.yamalHack.cdn.GcsResolver
import io.terra.yamalHack.dto.ContentLinksDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@Service
class ContentService(
        @Autowired val gcsResolver: GcsResolver,
        @Autowired val uploadService: UploadService
) {
    fun generateContentLinks(): ContentLinksDto {
        val filename = "${UUID.randomUUID()}"
        return gcsResolver.generateContentLinks(filename)
    }

    fun upload(file: File): String {
        val contentLinksDto = generateContentLinks()
        uploadService.uploadImage(file, contentLinksDto.uploadLink)
        return contentLinksDto.downloadLink
    }
}