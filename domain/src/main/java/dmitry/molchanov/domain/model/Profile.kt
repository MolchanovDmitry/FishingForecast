package dmitry.molchanov.domain.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Profile {
    abstract val name: String
}

@Serializable
data class SimpleProfile(override val name: String) : Profile()

@Serializable
data class CommonProfile(override val name: String) : Profile()
