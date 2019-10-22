package io.terra.yamalHack.api

import okhttp3.*
import org.springframework.stereotype.Component
import java.io.IOException
import java.io.File


@Component
class UploadService {

    @Throws(IOException::class)
    fun uploadImage(image: File, url: String) {

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", image.absolutePath, RequestBody.create(MEDIA_TYPE_JPG, image))
                .build()

        val request = Request.Builder().url(url)
                .post(requestBody).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Unexpected code $response")
        }

    }

    companion object {

        private val MEDIA_TYPE_JPG = MediaType.parse("image/jpg")
    }

}