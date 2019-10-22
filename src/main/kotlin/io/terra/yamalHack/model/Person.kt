package io.terra.yamalHack.model

import org.springframework.data.annotation.Id

data class Person(
        @Id
        val id: String,
        val name: String,
        val userData: String,
        val persistedFaces: List<String>
)