package io.terra.yamalHack.service

import io.terra.yamalHack.api.FaceApi
import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import io.terra.yamalHack.util.CommandLineTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


@Component
class FaceService(
        @Autowired val faceApi: FaceApi,
        @Autowired val commandLineTool: CommandLineTool,
        @Autowired val contentService: ContentService
) {

    fun isFacePresent(imagePath: String): Boolean {
        var result = commandLineTool.runTool(imagePath)
        result = result.substringBefore("\n")
        return result.toInt() > 0
    }

    fun isFacePresent(image: MultipartFile): Boolean {
        val imagePath = saveImage(image)
        var result = commandLineTool.runTool(imagePath)
        result = result.substringBefore("\n")
        return result.toInt() > 0
    }

    private fun saveImage(image: MultipartFile): String {
        val file = File("src/main/resources/images/${UUID.randomUUID().toString()}.jpg")
        file.createNewFile()
        file.writeBytes(image.bytes)
        return file.absolutePath
    }


    fun detectFace(file: MultipartFile): List<DetectFaceApiResponse> {

        val imagePath = saveImage(file)
        val isFace = isFacePresent(imagePath)
        val result: List<DetectFaceApiResponse>
        result = if(isFace) {
            faceApi.detectFace(file)
        } else {
            emptyList()
        }
        Files.delete(Paths.get(imagePath))
        return result
    }

    fun identify(faceId: String, personGroupId: String): List<IdentifyFaceApiResponse> {
        return faceApi.identify(IdentifyData(personGroupId, listOf(faceId)))
    }
}