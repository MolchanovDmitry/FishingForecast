package dmitry.molchanov.fishingforecast.model

/**
 * Профиль прогнозирования.
 * Например, профиль конкретного человека, времени года или типы рыбалки
 */
data class Profile(val name: String)

/** Общий профиль, под котором будут выводиться все точки всех профилей. */
val commonProfile = Profile(name = "")
