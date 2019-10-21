package io.terra.yamalHack.api

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
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

}