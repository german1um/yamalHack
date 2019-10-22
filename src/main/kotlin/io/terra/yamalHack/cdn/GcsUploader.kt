package io.terra.yamalHack.cdn

import io.terra.yamalHack.service.FaceService
import okhttp3.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component
class GcsUploader {
    private val logger = LoggerFactory.getLogger(GcsUploader::class.java)

    val client = OkHttpClient()

    fun loadImageToGcs(url: String, file: MultipartFile) {
        logger.info("Load image to cdn")

        val mediaType = MediaType.parse("image/png")
        val body = RequestBody.create(mediaType, file.bytes)

        val request = Request.Builder()
                .url(url)
                .put(body) //PUT
                .build()

        client.newCall(request).execute()
    }

}