package io.terra.yamalHack.service

import io.terra.yamalHack.api.FaceApi
import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class FaceService(
        @Autowired val faceApi: FaceApi
) {

    fun detectFace(imageUrl: String): List<DetectFaceApiResponse> {
        return faceApi.detectFace(Image(imageUrl))
    }

    fun identify(faceId: String, personGroupId: String): List<IdentifyFaceApiResponse> {
        return faceApi.identify(IdentifyData(personGroupId, listOf(faceId)))
    }
}