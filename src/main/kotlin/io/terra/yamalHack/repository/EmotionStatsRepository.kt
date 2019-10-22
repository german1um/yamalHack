package io.terra.yamalHack.repository

import io.terra.yamalHack.model.EmotionStats
import io.terra.yamalHack.model.Parent
import org.springframework.data.mongodb.repository.MongoRepository

interface EmotionStatsRepository : MongoRepository<EmotionStats, String> {
}