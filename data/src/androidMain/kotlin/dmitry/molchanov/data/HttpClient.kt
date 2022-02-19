package dmitry.molchanov.data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import okhttp3.logging.HttpLoggingInterceptor

actual val httpClient = HttpClient(OkHttp) {
    engine {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)
    }
    install(JsonFeature) {
        val jsonDecoder = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        }
        serializer = KotlinxSerializer(jsonDecoder)
    }
}