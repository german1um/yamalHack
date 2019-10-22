package io.terra.yamalHack.service

import io.terra.yamalHack.model.PersistedFace
import io.terra.yamalHack.model.Person
import io.terra.yamalHack.model.PersonGroup
import io.terra.yamalHack.repository.PersonGroupRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PersonGroupService(
        @Autowired val personGroupRepository: PersonGroupRepository
){

    fun addFaceToPerson(groupId: String, personId: String, face: PersistedFace) {
        val group = loadOrThrow(groupId)
        val person = loadPersonFromGroup(group, personId)

        val newPerson = Person(
                id = person.id,
                name = person.name,
                userData = person.userData,
                persistedFaces = person.persistedFaces + face
        )

        val newGroup = group.updatePerson(newPerson)

        personGroupRepository.save(
                PersonGroup(
                        id = newGroup.id,
                        name = newGroup.name,
                        userData = newGroup.userData,
                        persons = newGroup.persons
                )
        )
    }

    fun addPersonToGroup(groupId: String, person: Person) {
        val group = loadOrThrow(groupId)

        personGroupRepository.save(
                PersonGroup(
                        id = group.id,
                        name = group.name,
                        userData = group.userData,
                        persons = group.persons + person
                )
        )
    }

    fun setTrainedStatus(groupId: String, newStatus: Boolean) {
        val group = loadOrThrow(groupId)

        if (group.isTrained == newStatus) return

        personGroupRepository.save(
                PersonGroup(
                        id = group.id,
                        name = group.name,
                        userData = group.userData,
                        persons = group.persons,
                        isTrained = newStatus
                )
        )
    }

    fun loadOrThrow(groupId: String): PersonGroup {
        return personGroupRepository.findById(groupId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Person Group Not Found")
        }
    }

    fun save(personGroup: PersonGroup) {
        personGroupRepository.save(personGroup)
    }

    private fun loadPersonFromGroup(group: PersonGroup, personId: String): Person {
        return group.persons.firstOrNull { it.id == personId } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found in this group")
    }

}