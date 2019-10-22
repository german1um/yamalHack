package io.terra.yamalHack.repository

import io.terra.yamalHack.model.Parent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface ParentRepository : MongoRepository<Parent, String> {

    fun findByToken(@Param("token") token: String): Optional<Parent>

}