package io.terra.yamalHack.dto

data class PersonGroupDto(
        val name: String,
        val userData: String,
        val recognitionModel: String = "recognition_02"
)