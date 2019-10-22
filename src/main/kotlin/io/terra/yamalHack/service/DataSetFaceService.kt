package io.terra.yamalHack.service

import io.terra.yamalHack.api.DataSetFaceApi
import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.dto.PersonGroupDto
import io.terra.yamalHack.model.Image
import io.terra.yamalHack.model.PersistedFace
import io.terra.yamalHack.model.Person
import io.terra.yamalHack.model.PersonGroup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataSetFaceService (
        @Autowired val dataSetFaceApi: DataSetFaceApi,
        @Autowired val personGroupService: PersonGroupService
) {

    fun createPersonGroup(personGroupDto: PersonGroupDto): String {
        val groupId = dataSetFaceApi.createPersonGroup(personGroupDto)

        personGroupService.save(
                PersonGroup(
                        id = groupId,
                        name = personGroupDto.name,
                        userData = personGroupDto.userData
                )
        )

        return groupId
    }

    fun createPersonInGroup(groupId: String, personDto: PersonDto): String {
        val personId = dataSetFaceApi.createPersonInGroup(groupId, personDto)

        personGroupService.addPersonToGroup(
                groupId,
                Person(
                        id = personId,
                        name = personDto.name,
                        userData = personDto.userData
                )
        )

        return personId
    }

    fun addFaceToPerson(groupId: String, personId: String, imageUrl: String): String {
        val faceId = dataSetFaceApi.addFaceToUser(groupId, personId, Image(imageUrl))

        personGroupService.addFaceToPerson(
                groupId,
                personId,
                PersistedFace(
                        id = faceId,
                        url = imageUrl
                )
        )

        return faceId
    }

    fun trainPersonGroup(groupId: String): String {
        return dataSetFaceApi.trainPersonGroup(groupId)
    }

    fun trainingStatus(groupId: String): String {
        val status= dataSetFaceApi.trainingStatus(groupId)

        if (status == "succeeded") {
            personGroupService.setTrainedStatus(groupId, true)
        }

        return status
    }
}