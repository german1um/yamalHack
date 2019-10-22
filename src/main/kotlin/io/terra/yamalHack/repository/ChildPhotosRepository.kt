package io.terra.yamalHack.repository

import io.terra.yamalHack.model.ChildPhotos
import io.terra.yamalHack.model.FriendsStats
import org.springframework.data.mongodb.repository.MongoRepository

interface ChildPhotosRepository : MongoRepository<ChildPhotos, String> {



}