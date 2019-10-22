package io.terra.yamalHack.model

import org.springframework.data.annotation.Id

data class PersonGroup(
        @Id
        val id: String,
        val name: String,
        val userData: String,
        val persons: List<Person>
)