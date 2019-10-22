package io.terra.yamalHack.api

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component
@PropertySource("classpath:token.properties")
class FaceApi (
        @Autowired
        val env: Environment
){

    private val logger = LoggerFactory.getLogger(FaceApi::class.java)

    private var token: String = env.getProperty("secret-key")?:""
    private var baseUrl: String = env.getProperty("endpoint")?:""

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val api: FaceApiService

    init {
        api = retrofit.create(FaceApiService::class.java)
    }


    fun detectFace(image: Image): List<DetectFaceApiResponse> {
        val response = api.detectFace(
                subscriptionKey = token,
                imageUrl = image
        ).execute()

        return response.body()!!
    }

    fun identify(data: IdentifyData): List<IdentifyFaceApiResponse> {
        val response = api.identify(
                subscriptionKey = token,
                identifyData = data
        ).execute()

        return response.body()!!
    }
}