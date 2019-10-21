package io.terra.yamalHack.model

data class PersonGroup(
        val name: String,
        val userData: String,
        val recognitionModel: String = "recognition_02"
)