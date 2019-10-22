package io.terra.yamalHack.model

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Parent(
        val token: String,
        val name: String = "Gordon",
        val children: List<ChildDto> = emptyList(),
        val id: String = UUID.randomUUID().toString()
)

data class ChildDto(
        val id: String,
        val photoCount: Int
)