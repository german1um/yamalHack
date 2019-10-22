package io.terra.yamalHack.api.entity

data class PersonGroupTrainingStatusResponse(
        val status: String,
        val createdDateTime: String,
        val lastActionDateTime: String,
        val message: String
)