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
        val childId: String,
        val emotions: Emotion
)

@Document
data class ActionStats(
        val timestamp: Long,
        val childId: String,
        val cameraId: String,
        val x: Int,
        val y: Int
)

@Document
data class ChildPhotos(
        val timestamp: Long,
        val url: String,
        val childIds: List<String>
)