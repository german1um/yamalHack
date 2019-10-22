package io.terra.yamalHack.api.entity

data class IdentifyResponse(
        val faceId: String,
        val candidates: List<IdentifyCandidate>
)

data class IdentifyCandidate(
        val personId: String,
        val confidence: Double
)