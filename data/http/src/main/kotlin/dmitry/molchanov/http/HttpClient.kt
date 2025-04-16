package dmitry.molchanov.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient = HttpClient(OkHttp) {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println("HTTP Client: $message")
            }
        }
        level = LogLevel.BODY
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }
}
