package io.terra.yamalHack.model

import io.terra.yamalHack.api.entity.Emotion
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class FriendsStats(
        val timestamp: Long,
        val children: List<String>
)

@Document
data class EmotionStats(
        val timestamp: Long,
        val emotions: Emotion
)

@Document
data class ActionStats(
        val timestamp: Long,
        val cameraId: String,
        val x: Int,
        val y: Int
)