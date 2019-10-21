package io.terra.yamalHack.api.entity

data class DetectFaceApiResponse (
        val faceId: String,
        val faceAttributes: FaceAttributes
)