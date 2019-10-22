package io.terra.yamalHack.repository

import io.terra.yamalHack.model.Person
import org.springframework.data.mongodb.repository.MongoRepository

interface PersonRepository : MongoRepository<Person, String> {

}