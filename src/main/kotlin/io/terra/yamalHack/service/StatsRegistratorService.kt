package io.terra.yamalHack.service

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
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
        registerFriendsStats(identifications)

        identifications.forEach { identify ->
            val detect = detections.first { it.faceId == identify.faceId }
            registerChild(detect, identify)
        }

        registerPhoto(identifications, file)
    }

    private fun registerChild(detect: DetectFaceApiResponse, identify: IdentifyFaceApiResponse) {
        registerEmotionStats(detect)
        registerActionStats(detect, identify.matcherCandidateId() ?: "")
    }

    private fun registerFriendsStats(identifications: List<IdentifyFaceApiResponse>) {
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
    }

    private fun registerEmotionStats(detect: DetectFaceApiResponse) {
        emotionStatsRepository.save(
                EmotionStats(
                        System.currentTimeMillis(),
                        detect.faceId,
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
        val links = gcsResolver.generateContentLinks(UUID.randomUUID().toString())

        gcsUploader.loadImageToGcs(links.uploadLink, file)

        childPhotosRepository.save(
                ChildPhotos(
                       links.downloadLink,
                        identifications.mapNotNull { it.matcherCandidateId() }
                )
        )
    }
}