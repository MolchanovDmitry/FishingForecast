package dmitry.molchanov.fishingforecast.android.mapper

import android.content.Context
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.model.Profile

/**
 * Получатель общего профиля
 */
class CommonProfileFetcher(context: Context) {

    val name: String = context.resources.getString(R.string.common_profile)

    fun get(): Profile = Profile(name, isCommon = true)
}