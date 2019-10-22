package io.terra.yamalHack.service

import io.terra.yamalHack.api.FaceApi
import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.service.ParentService.Companion.tmpGroupId
import io.terra.yamalHack.util.CommandLineTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


@Component
class FaceService(
        @Autowired val faceApi: FaceApi,
        @Autowired val personGroupService: PersonGroupService,
        @Autowired val commandLineTool: CommandLineTool,
        @Autowired val contentService: ContentService,
        @Autowired val statsRegistratorService: StatsRegistratorService
) {

    private val logger = LoggerFactory.getLogger(FaceService::class.java)

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
        val file = File("${UUID.randomUUID().toString()}.jpg")
        file.createNewFile()
        file.writeBytes(image.bytes)
        return file.absolutePath
    }


    fun detectFace(file: MultipartFile): List<DetectFaceApiResponse> {

        val imagePath = saveImage(file)
        val isFace = isFacePresent(imagePath)
        val result: List<DetectFaceApiResponse>
        result = if (isFace) {
            faceApi.detectFace(file)
        } else {
            emptyList()
        }
        Files.delete(Paths.get(imagePath))
        return result
    }

    fun identify(faceIds: List<String>, personGroupId: String): List<IdentifyFaceApiResponse> {
        val group = personGroupService.loadOrThrow(personGroupId)

        if (!group.isTrained) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person Group is not trained yet!")

        return faceApi.identify(IdentifyData(personGroupId, faceIds))
    }

    fun processCameraPic(file: MultipartFile) {

        logger.info("Start process camera pic")
        val faces = detectFace(file)
        logger.info("Detect ${faces.size} faces")


        val faceIds = faces.map {
            it.faceId
        }

        if(faces.isNotEmpty()) {
            statsRegistratorService.register(
                    faces,
                    identify(faceIds, tmpGroupId),
                    file
            )
        }

        logger.info("Finish process camera pic")
    }
}