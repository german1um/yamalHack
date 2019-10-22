package io.terra.yamalHack.model

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Parent(
        val token: String,
        val name: String = "Gordon",
        val childrenIds: List<String> = emptyList(),
        val id: String = UUID.randomUUID().toString()
)