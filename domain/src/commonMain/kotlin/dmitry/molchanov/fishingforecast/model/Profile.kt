package dmitry.molchanov.fishingforecast.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Profile(open val name: String)
data class SimpleProfile(override val name: String) : Profile(name)
data class CommonProfile(override val name: String) : Profile(name)