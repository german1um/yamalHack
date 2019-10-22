package io.terra.yamalHack.api

import io.terra.yamalHack.api.entity.AddFaceResponse
import io.terra.yamalHack.api.entity.CreatePersonResponse
import io.terra.yamalHack.api.entity.IdentifyResponse
import io.terra.yamalHack.api.entity.PersonGroupTrainingStatusResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import io.terra.yamalHack.dto.PersonDto
import io.terra.yamalHack.dto.PersonGroupDto
import retrofit2.Call
import retrofit2.http.*

interface DataSetFaceApiService {

    @PUT("persongroups/{personGroupId}")
    fun createPersonGroup(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String,
            @Body personGroupDto: PersonGroupDto
    ): Call<Void>

    @POST("persongroups/{personGroupId}/persons")
    fun createPersonInGroup(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String,
            @Body personDto: PersonDto
    ): Call<CreatePersonResponse>

    @POST("persongroups/{personGroupId}/persons/{personId}/persistedFaces")
    fun addFaceToPerson(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String,
            @Path("personId") personId: String,
            @Body imageUrl: Image
    ): Call<AddFaceResponse>

    @POST("persongroups/{personGroupId}/train")
    fun trainPersonGroup(
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String
    ): Call<Void>

    @GET("persongroups/{personGroupId}/training")
    fun trainingStatus(
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String
    ): Call<PersonGroupTrainingStatusResponse>

    @POST("identify")
    fun identify(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Body identifyData: IdentifyData
    ): Call<List<IdentifyResponse>>
}