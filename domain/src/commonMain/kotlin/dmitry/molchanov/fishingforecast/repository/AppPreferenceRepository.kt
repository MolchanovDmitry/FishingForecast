package dmitry.molchanov.fishingforecast.repository

interface AppPreferenceRepository {

    var lastRequestTime: Long?
}