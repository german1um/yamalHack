package io.terra.yamalHack.controller

import io.terra.yamalHack.dto.ContentLinksDto
import io.terra.yamalHack.service.ContentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/content")
class ContentController(
        @Autowired val contentService: ContentService
) {
    @GetMapping("/generateContentLinks")
    fun generateUploadLink(): ContentLinksDto {
        return contentService.generateContentLinks()
    }
}