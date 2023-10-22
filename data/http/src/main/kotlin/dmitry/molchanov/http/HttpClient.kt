package dmitry.molchanov.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import okhttp3.logging.HttpLoggingInterceptor

val httpClient =
    HttpClient(OkHttp) {
        engine {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)
        }

        install(JsonFeature) {
            val jsonDecoder =
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                }
            serializer = KotlinxSerializer(jsonDecoder)
        }
    }
