package io.terra.yamalHack.api

import io.terra.yamalHack.api.entity.DetectFaceApiResponse
import io.terra.yamalHack.api.entity.IdentifyFaceApiResponse
import io.terra.yamalHack.dto.IdentifyData
import io.terra.yamalHack.model.Image
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import java.io.File

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

    fun detectFace(file: MultipartFile): List<DetectFaceApiResponse> {

        val requestFile = RequestBody.create(
                MediaType.parse("application/octet-stream"),
                file.bytes)
        val response = api.detectFace(
                subscriptionKey = token,
                file = requestFile
        ).execute()

        return response.body()!!
    }

    fun identify(data: IdentifyData): List<IdentifyFaceApiResponse> {
        logger.info("Making a call to azure identify")
        val response = api.identify(
                subscriptionKey = token,
                identifyData = data
        ).execute()
        logger.info("Making a call to azure identify - DONE")
        if(response.body() == null) {
            logger.info(response.message())
        }
        return response.body()?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, response.message())
    }
}