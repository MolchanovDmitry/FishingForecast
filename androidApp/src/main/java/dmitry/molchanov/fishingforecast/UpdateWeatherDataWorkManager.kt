package dmitry.molchanov.fishingforecast

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dmitry.molchanov.domain.usecase.FetchAndSaveWeatherDataUseCase
import dmitry.molchanov.domain.usecase.GetMapPointsUseCase
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
                        //.onSuccess(::updateWidgetViews)
                        .onFailure (::println)
                }
            }.awaitAll()
        }
        return Result.success()
    }

    /*private fun updateWidgetViews(weatherDataUpdate: WeatherDataUpdate) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.widget)
        val thisWidget = ComponentName(context, MyWidget::class.java)
        remoteViews.setTextViewText(R.id.my_text_view, "myText" + System.currentTimeMillis())
        appWidgetManager.updateAppWidget(thisWidget, remoteViews)
    }*/
}