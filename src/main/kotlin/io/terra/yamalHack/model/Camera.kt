package io.terra.yamalHack.model

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Camera(
        val id: String = UUID.randomUUID().toString()
)