package io.terra.yamalHack.repository

import io.terra.yamalHack.model.EmotionStats
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param

interface EmotionStatsRepository : MongoRepository<EmotionStats, String> {

    fun findByChildId(@Param("childId") childId: String): List<EmotionStats>

}