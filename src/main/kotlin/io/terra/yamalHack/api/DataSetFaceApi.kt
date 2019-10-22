package io.terra.yamalHack.api

import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.dto.PersonGroupDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@Component
@PropertySource("classpath:token.properties")
class DataSetFaceApi(
        @Autowired
        val env: Environment
) {

    private val logger = LoggerFactory.getLogger(DataSetFaceApi::class.java)

    private var token: String = env.getProperty("secret-key") ?: ""
    private var baseUrl: String = env.getProperty("endpoint") ?: ""

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val api: DataSetFaceApiService

    init {
        api = retrofit.create(DataSetFaceApiService::class.java)
    }

    fun createPersonGroup(personGroupDto: PersonGroupDto): String {
        val groupId = UUID.randomUUID().toString()

        val response = api.createPersonGroup(
                subscriptionKey = token,
                personGroupId = groupId,
                personGroupDto = personGroupDto
        ).execute()

        return groupId
    }

    fun createPersonInGroup(groupId: String, personDto: PersonDto): String {
        val response = api.createPersonInGroup(
                subscriptionKey = token,
                personGroupId = groupId,
                personDto = personDto
        ).execute()

        return response.body()?.personId ?: ""
    }

    fun addFaceToUser(groupId: String, personId: String, image: Image): String {
        val response = api.addFaceToPerson(
                subscriptionKey = token,
                personGroupId = groupId,
                personId = personId,
                imageUrl = image
        ).execute()

        return response.body()?.persistedFaceId ?: ""
    }

    fun trainPersonGroup(personGroupId: String): String {
        val response = api.trainPersonGroup(
                subscriptionKey = token,
                personGroupId = personGroupId
        ).execute()

        return personGroupId
    }

    fun trainingStatus(personGroupId: String): String {
        val response = api.trainingStatus(
                subscriptionKey = token,
                personGroupId = personGroupId
        ).execute()

        return response.body()?.status ?: "FAIL"
    }
}