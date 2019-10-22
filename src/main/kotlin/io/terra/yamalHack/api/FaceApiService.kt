package io.terra.yamalHack.api

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import retrofit2.Call
import retrofit2.http.*

interface FaceApiService {

    @POST("detect")
    fun detectFace(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Query("returnFaceId") faceId: Boolean = true,
            @Query("returnFaceLandmarks") fceLandmarks: Boolean = true,
            @Query("returnFaceAttributes") faceAttributes: String = "emotion",
            @Body imageUrl: Image

    ): Call<List<DetectFaceApiResponse>>

    @POST("identify")
    fun identify(
            @Header("Content-Type") contentType: String = "application/json",
            @Header("Ocp-Apim-Subscription-Key") subscriptionKey: String,
            @Body identifyData: IdentifyData
    ): Call<List<IdentifyFaceApiResponse>>
}