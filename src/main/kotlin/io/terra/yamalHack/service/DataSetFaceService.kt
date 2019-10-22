package io.terra.yamalHack.service

import io.terra.yamalHack.api.DataSetFaceApi
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.dto.PersonGroupDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataSetFaceService (
        @Autowired val dataSetFaceApi: DataSetFaceApi
) {

    fun createPersonGroup(personGroupDto: PersonGroupDto): String {
        return dataSetFaceApi.createPersonGroup(personGroupDto)
    }

    fun createPersonInGroup(groupId: String, personDto: PersonDto): String {
        return dataSetFaceApi.createPersonInGroup(groupId, personDto)
    }

    fun addFaceToPerson(groupId: String, personId: String, imageUrl: String): String {
        return dataSetFaceApi.addFaceToUser(groupId, personId, Image(imageUrl))
    }

    fun trainPersonGroup(groupId: String): String {
        return dataSetFaceApi.trainPersonGroup(groupId)
    }

    fun trainingStatus(groupId: String): String {
        return dataSetFaceApi.trainingStatus(groupId)
    }
}