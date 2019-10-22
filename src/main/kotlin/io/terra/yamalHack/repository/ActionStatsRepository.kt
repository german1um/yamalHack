package io.terra.yamalHack.repository

import io.terra.yamalHack.model.ActionStats
import io.terra.yamalHack.model.Parent
import org.springframework.data.mongodb.repository.MongoRepository

interface ActionStatsRepository : MongoRepository<ActionStats, String> {

}