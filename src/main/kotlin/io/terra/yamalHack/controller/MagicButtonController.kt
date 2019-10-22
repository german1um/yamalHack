package io.terra.yamalHack.controller

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.service.FaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/faceRecognition")
class MagicButtonController (
        @Autowired val faceService: FaceService
){

    @GetMapping
    fun recognize(
            @RequestParam("imageUrl")
            picture: String
    ): List<DetectFaceApiResponse> {
        return faceService.detectFace(picture)
    }
}