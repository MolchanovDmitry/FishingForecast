package dmitry.molchanov.data

import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Начальный вариант HTTP клиента.
 * В будущем будет переопределен для каждой из платформ. На раннем этапе обойдемся общим.
 */
object NetworkClient {

    suspend inline fun <reified T> loadData(url: String, headers: Map<String, String>): Result<T> =
        kotlin.runCatching {
            httpClient.get(url) {
                method = HttpMethod.Get
                headers {
                    headers.forEach {
                        append(it.key, it.value)
                    }
                }
            }
        }
}