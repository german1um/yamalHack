package io.terra.yamalHack.controller

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.service.FaceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/face")
class FaceController (
        @Autowired val faceService: FaceService
){

    @PostMapping("/detect")
    fun detect(
            @RequestParam("imageUrl")
            file: MultipartFile
    ): List<DetectFaceApiResponse> {
        return faceService.detectFace(file)
    }

    @PostMapping("/identify")
    fun identify(
            @RequestParam("faceId") faceId: String,
            @RequestParam("groupId") groupId: String
    ): List<IdentifyFaceApiResponse> {
        return faceService.identify(faceId, groupId)
    }

    @PostMapping("/uploadImage")
    fun uploadImage(@RequestParam("file") file: MultipartFile): String {

        return """
           Pic - ${faceService.isFacePresent(file)}
        """.trimIndent()
    }
}
