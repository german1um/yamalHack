package io.terra.yamalHack.service

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.api.entity.numbOfMatchers
import io.terra.yamalHack.cdn.GcsResolver
import io.terra.yamalHack.cdn.GcsUploader
import io.terra.yamalHack.model.ActionStats
import io.terra.yamalHack.model.ChildPhotos
import io.terra.yamalHack.model.EmotionStats
import io.terra.yamalHack.model.FriendsStats
import io.terra.yamalHack.repository.ActionStatsRepository
import io.terra.yamalHack.repository.ChildPhotosRepository
import io.terra.yamalHack.repository.EmotionStatsRepository
import io.terra.yamalHack.repository.FriendsStatsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class StatsRegistratorService(
        @Autowired val friendsStatsRepository: FriendsStatsRepository,
        @Autowired val emotionStatsRepository: EmotionStatsRepository,
        @Autowired val actionStatsRepository: ActionStatsRepository,
        @Autowired val childPhotosRepository: ChildPhotosRepository,
        @Autowired val gcsResolver: GcsResolver,
        @Autowired val gcsUploader: GcsUploader
) {
    private val logger = LoggerFactory.getLogger(StatsRegistratorService::class.java)

    fun loadActionStats(childId: String): List<ActionStats> {
        return actionStatsRepository.findByChildId(childId)
    }

    fun loadFriendsStats(): List<FriendsStats> {
        return friendsStatsRepository.findAll()
    }

    fun loadEmotionsForChild(childId: String): List<EmotionStats> {
        return emotionStatsRepository.findByChildId(childId)
    }

    fun register(detections: List<DetectFaceApiResponse>, identifications: List<IdentifyFaceApiResponse>, file: MultipartFile) {
        logger.info("Start registration")
        val matchers = identifications.numbOfMatchers()
        logger.info("Identificated faces: ${matchers}")

        if (matchers == 0) {
            logger.info("Finish registration")
            return
        }

        registerFriendsStats(identifications)

        identifications.forEach { identify ->
            val detect = detections.first { it.faceId == identify.faceId }
            registerChild(detect, identify)
        }

        registerPhoto(identifications, file)

        logger.info("Finish registration")
    }

    private fun registerChild(detect: DetectFaceApiResponse, identify: IdentifyFaceApiResponse) {
        registerEmotionStats(detect, identify.matcherCandidateId() ?: "")
        registerActionStats(detect, identify.matcherCandidateId() ?: "")
    }

    private fun registerFriendsStats(identifications: List<IdentifyFaceApiResponse>) {
        logger.info("Start register best friends")
        friendsStatsRepository.save(
                FriendsStats(
                        System.currentTimeMillis(),
                        identifications.map { identify ->
                            identify.matcherCandidateId() ?: ""
                        }.filter {
                            it.isNotBlank()
                        }
                )
        )
        logger.info("Finish register best friends")
    }

    private fun registerEmotionStats(detect: DetectFaceApiResponse, childId: String) {
        emotionStatsRepository.save(
                EmotionStats(
                        System.currentTimeMillis(),
                        childId,
                        detect.faceAttributes.emotion
                )
        )
    }

    private fun registerActionStats(detect: DetectFaceApiResponse, childId: String) {
        if (childId.isBlank()) return

        actionStatsRepository.save(
                ActionStats(
                        System.currentTimeMillis(),
                        childId,
                        "",
                        detect.faceRectangle.left,
                        detect.faceRectangle.top
                )
        )
    }

    private fun registerPhoto(identifications: List<IdentifyFaceApiResponse>, file: MultipartFile) {
        logger.info("Start register photo")
        val links = gcsResolver.generateContentLinks(UUID.randomUUID().toString())

        gcsUploader.loadImageToGcs(links.uploadLink, file)

        logger.info("Image download link: ${links.downloadLink}")

        childPhotosRepository.save(
                ChildPhotos(
                       links.downloadLink,
                        identifications.mapNotNull { it.matcherCandidateId() }
                )
        )
        logger.info("Finish register photo")

    }
}