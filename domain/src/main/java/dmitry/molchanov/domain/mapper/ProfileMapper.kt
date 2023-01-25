package dmitry.molchanov.domain.mapper

import dmitry.molchanov.domain.model.SimpleProfile

class ProfileMapper(private val commonProfileFetcher: CommonProfileFetcher) {

    fun mapProfile(profileName: String?) = profileName?.let(::SimpleProfile) ?: commonProfileFetcher.instance

    fun mapToProfiles(profileNames: List<String>) = profileNames.map(::SimpleProfile) + commonProfileFetcher.instance
}
