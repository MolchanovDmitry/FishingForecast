package dmitry.molchanov.fishingforecast

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dmitry.molchanov.domain.usecase.FetchAndSaveWeatherDataUseCase
import dmitry.molchanov.domain.usecase.GetMapPointsUseCase
import dmitry.molchanov.domain.usecase.WeatherDataUpdate
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import org.koin.java.KoinJavaComponent.get


class UpdateWeatherDataWorkManager(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val getMapPointsUseCase = get<GetMapPointsUseCase>(GetMapPointsUseCase::class.java)
        val getWeatherDataUseCase =
            get<FetchAndSaveWeatherDataUseCase>(FetchAndSaveWeatherDataUseCase::class.java)
        supervisorScope {
            val mapPoints = getMapPointsUseCase.execute()
            mapPoints.map { mapPoint ->
                async {
                    getWeatherDataUseCase.execute(mapPoint)
                        .onSuccess(::updateWidgetViews)
                        .onFailure(::println)
                }
            }.awaitAll()
        }
        return Result.success()
    }

    private fun updateWidgetViews(weatherDataUpdate: WeatherDataUpdate) {
        val weatherDataDate = weatherDataUpdate.lastWeatherDataDate
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.widget)
        val thisWidget = ComponentName(applicationContext, FishWidgetProvider::class.java)
        remoteViews.setTextViewText(
            R.id.lastWeatherDate,
            weatherDataDate.run { "$year.$month.$day.$dayPart" }
        )
        appWidgetManager.updateAppWidget(thisWidget, remoteViews)
    }
}