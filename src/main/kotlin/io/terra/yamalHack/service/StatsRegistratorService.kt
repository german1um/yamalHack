package io.terra.yamalHack.service

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.model.ActionStats
import io.terra.yamalHack.model.EmotionStats
import io.terra.yamalHack.model.FriendsStats
import io.terra.yamalHack.repository.ActionStatsRepository
import io.terra.yamalHack.repository.EmotionStatsRepository
import io.terra.yamalHack.repository.FriendsStatsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StatsRegistratorService(
        @Autowired val friendsStatsRepository: FriendsStatsRepository,
        @Autowired val emotionStatsRepository: EmotionStatsRepository,
        @Autowired val actionStatsRepository: ActionStatsRepository
) {

    fun register(detections: List<DetectFaceApiResponse>, identifications: List<IdentifyFaceApiResponse>) {
        registerFriendsStats(identifications)

        identifications.forEach { identify ->
            val detect = detections.first { it.faceId == identify.faceId }
            registerChild(detect, identify)
        }
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

}