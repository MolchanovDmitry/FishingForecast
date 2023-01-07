package dmitry.molchanov.fishingforecast.mapper

import dmitry.molchanov.fishingforecast.model.SimpleProfile

class ProfileMapper(private val commonProfileFetcher: CommonProfileFetcher) {

    fun mapProfile(profileName: String?) = profileName?.let(::SimpleProfile) ?: commonProfileFetcher.instance

    fun mapToProfiles(profileNames: List<String>) = profileNames.map(::SimpleProfile) + commonProfileFetcher.instance
}
