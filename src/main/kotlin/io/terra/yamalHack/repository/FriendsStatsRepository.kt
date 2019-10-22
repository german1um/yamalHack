package io.terra.yamalHack.repository

import io.terra.yamalHack.model.FriendsStats
import org.springframework.data.mongodb.repository.MongoRepository


interface FriendsStatsRepository : MongoRepository<FriendsStats, String> {
}