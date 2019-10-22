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

    @PostMapping("/cameraPic")
    fun cameraPic(
            @RequestBody
            file: MultipartFile
    ) {
        faceService.processCameraPic(file)
    }
}
