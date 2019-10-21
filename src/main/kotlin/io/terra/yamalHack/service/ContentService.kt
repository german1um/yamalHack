package io.terra.yamalHack.service

import io.terra.yamalHack.cdn.GcsResolver
import io.terra.yamalHack.dto.ContentLinksDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ContentService(
        @Autowired val gcsResolver: GcsResolver
) {
    fun generateContentLinks(): ContentLinksDto {
        val filename = "${UUID.randomUUID()}"
        return gcsResolver.generateContentLinks(filename)
    }
}