object Modules {

    private const val FEATURE = ":feature"
    private const val DATA = ":data"
    private const val REPOSITORY = ":repository"

    const val CORE = ":core"
    const val DOMAIN = ":domain"
    const val DB = "$DATA:db"
    const val HTTP = "$DATA:http"
    const val PREFERENCE = "$DATA:preference"
    const val WEATHER_REMOTE = "$DATA:weather-remote"
    const val WEATHER_DATA_UPDATE = "$FEATURE:weather-data-update"
    const val PROFILE = "$DATA$REPOSITORY:profile"

}