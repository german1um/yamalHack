package io.terra.yamalHack.api.entity

import com.microsoft.azure.cognitiveservices.faceapi.FaceAttributes

data class DetectFaceApiResponse (
        val faceId: String,
        val faceAttributes: FaceAttributes
)