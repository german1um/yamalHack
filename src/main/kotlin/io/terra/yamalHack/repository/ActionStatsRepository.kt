package io.terra.yamalHack.repository

import io.terra.yamalHack.model.ActionStats
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param

interface ActionStatsRepository : MongoRepository<ActionStats, String> {

    fun findByChildId(@Param("childId") childId: String): List<ActionStats>

}