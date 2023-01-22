package dmitry.molchanov.fishingforecast.android.mapper

import android.content.Context
import dmitry.molchanov.domain.model.CommonProfile
import dmitry.molchanov.fishingforecast.android.R
import dmitry.molchanov.fishingforecast.mapper.CommonProfileFetcher

/**
 * Получатель общего профиля
 */
class CommonProfileFetcherImpl(context: Context) : CommonProfileFetcher {

    private val name: String = context.resources.getString(R.string.common_profile)

    override val instance: CommonProfile = CommonProfile(name)
}
