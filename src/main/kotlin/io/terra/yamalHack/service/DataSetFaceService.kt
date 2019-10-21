package io.terra.yamalHack.service

import io.terra.yamalHack.api.DataSetFaceApi
import io.terra.yamalHack.model.Image
import io.terra.yamalHack.model.Person
import io.terra.yamalHack.model.PersonGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataSetFaceService (
        @Autowired val dataSetFaceApi: DataSetFaceApi
) {

    fun createPersonGroup(personGroup: PersonGroup): String {
        return dataSetFaceApi.createPersonGroup(personGroup)
    }

    fun createPersonInGroup(groupId: String, person: Person): String {
        return dataSetFaceApi.createPersonInGroup(groupId, person)
    }

    fun addFaceToPerson(groupId: String, personId: String, imageUrl: String): String {
        return dataSetFaceApi.addFaceToUser(groupId, personId, Image(imageUrl))
    }
}