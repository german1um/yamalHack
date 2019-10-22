package io.terra.yamalHack.model

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PersonGroup(
        val id: String,
        val name: String,
        val userData: String,
        val persons: List<Person> = emptyList(),
        val isTrained: Boolean = false
) {

    fun updatePerson(person: Person): PersonGroup {
        val newPersons = persons.toMutableList()
        newPersons.removeIf { it.id == person.id }
        newPersons.add(person)

        return PersonGroup(
                id,
                name,
                userData,
                newPersons
        )
    }
}

data class Person(
        val id: String,
        val name: String,
        val userData: String,
        val persistedFaces: List<PersistedFace> = emptyList()
)

data class PersistedFace(
        val id: String,
        val url: String
)