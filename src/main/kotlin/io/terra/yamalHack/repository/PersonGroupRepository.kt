package io.terra.yamalHack.repository

import io.terra.yamalHack.model.PersonGroup
import org.springframework.data.mongodb.repository.MongoRepository

interface PersonGroupRepository : MongoRepository<PersonGroup, String> {


}