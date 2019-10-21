package io.terra.yamalHack.cdn

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import io.terra.yamalHack.config.GcsConfig
import io.terra.yamalHack.dto.ContentLinksDto
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

@Component
class GcsResolver(
        @Autowired config: GcsConfig
) {

    private val expiration = 604800
    private val httpMethod = "PUT"

    private val serverUrl = config.serverUrl
    private val hostname = config.hostname
    private val canonicalUrl = config.canonicalUrl

    private val canonicalHeaders = "host:storage.googleapis.com\n"
    private val signedHeaders = "host"

    private val credentials = GoogleCredentials.fromStream(FileInputStream(config.configFilePath)) as ServiceAccountCredentials

    fun generateContentLinks(filename: String): ContentLinksDto {
        val uploadLink = generateSignedUrl(filename)
        val downloadLink = generateDownloadLink(filename)

        return ContentLinksDto(uploadLink, downloadLink)
    }

    private fun generateDownloadLink(filename: String): String {
        return "$serverUrl$filename"
    }

    private fun generateSignedUrl(filename: String): String {

        val datetimeNow = Date()
        val requestTimestamp = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'").format(datetimeNow)
        val datestamp = SimpleDateFormat("yyyyMMdd").format(datetimeNow)

        val clientEmail = credentials.clientEmail
        val credentialScope = "$datestamp/auto/storage/goog4_request"
        val credential = "$clientEmail/$credentialScope"

        val canonicalQueryString = generateCanonicalQueryString(credential, requestTimestamp)

        val signature = generateSignature(filename, canonicalQueryString, requestTimestamp, credentialScope)

        return "$hostname$canonicalUrl$filename?$canonicalQueryString&X-Goog-Signature=$signature"
    }

    private fun generateCanonicalQueryString(credential: String, requestTimestamp: String): String {
        val queryParameters = mutableMapOf<String, String>()
        queryParameters["X-Goog-Algorithm"] = "GOOG4-RSA-SHA256"
        queryParameters["X-Goog-Credential"] = credential
        queryParameters["X-Goog-Date"] = requestTimestamp
        queryParameters["X-Goog-Expires"] = expiration.toString()
        queryParameters["X-Goog-SignedHeaders"] = signedHeaders

        var canonicalQueryString = ""
        queryParameters.forEach { (key, value) ->
            val encodedKey = URLEncoder.encode(key)
            val encodedValue = URLEncoder.encode(value)
            canonicalQueryString += "$encodedKey=$encodedValue&"
        }

        return canonicalQueryString.removeSuffix("&")
    }

    private fun generateSignature(filename: String, canonicalQueryString: String, requestTimestamp: String, credentialScope: String): String {
        val canonicalRequest =
                "$httpMethod\n" +
                        "$canonicalUrl$filename\n" +
                        "$canonicalQueryString\n" +
                        "$canonicalHeaders\n" +
                        "$signedHeaders\n" +
                        "UNSIGNED-PAYLOAD"

        val canonicalRequestHash = DigestUtils.sha256Hex(canonicalRequest)

        val stringToSign =
                "GOOG4-RSA-SHA256\n" +
                        "$requestTimestamp\n" +
                        "$credentialScope\n" +
                        canonicalRequestHash

        return Hex.encodeHexString(credentials.sign(stringToSign.toByteArray()))
    }
}