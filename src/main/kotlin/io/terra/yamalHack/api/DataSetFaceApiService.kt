package io.terra.yamalHack.api

import io.terra.yamalHack.api.entity.AddFaceResponse
import io.terra.yamalHack.api.entity.CreatePersonResponse
import io.terra.yamalHack.model.Image
import io.terra.yamalHack.model.Person
import io.terra.yamalHack.model.PersonGroup
import retrofit2.Call
import retrofit2.http.*

interface DataSetFaceApiService {

    @PUT("persongroups/{personGroupId}")
    fun createPersonGroup(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String,
            @Body personGroup: PersonGroup
    ): Call<Void>

    @POST("persongroups/{personGroupId}/persons")
    fun createPersonInGroup(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String,
            @Body person: Person
    ): Call<CreatePersonResponse>

    @POST("persongroups/{personGroupId}/persons/{personId}/persistedFaces")
    fun addFaceToPerson(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Path("personGroupId") personGroupId: String,
            @Path("personId") personId: String,
            @Body imageUrl: Image
    ): Call<AddFaceResponse>
}