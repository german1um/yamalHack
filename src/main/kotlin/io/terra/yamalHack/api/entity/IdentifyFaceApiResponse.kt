package io.terra.yamalHack.api.entity

data class IdentifyFaceApiResponse(
        val faceId: String,
        val candidates: List<IdentifyCandidate>
) {
    fun matcherCandidateId(): String? {
        return candidates.filter {
            it.confidence > 0.6
        }.maxBy {
            it.confidence
        }?.personId
    }
}

data class IdentifyCandidate(
        val personId: String,
        val confidence: Double
)