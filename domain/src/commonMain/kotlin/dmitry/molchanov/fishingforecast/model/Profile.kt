package dmitry.molchanov.fishingforecast.model

sealed class Profile(open val name: String)
data class SimpleProfile(override val name: String) : Profile(name)
data class CommonProfile(override val name: String) : Profile(name)