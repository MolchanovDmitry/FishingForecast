package dmitry.molchanov.fishingforecast.model

/**
 * Профиль прогнозирования.
 * Например, профиль конкретного человека, времени года или типы рыбалки
 */
data class Profile(
    val name: String,
    val isCommon: Boolean,
)
