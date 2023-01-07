package dmitry.molchanov.domain.mapper

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> T.string() = Json.encodeToString(this)
inline fun <reified T> String.deserialize() = Json.decodeFromString<T>(this)