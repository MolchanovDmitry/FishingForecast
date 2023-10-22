package dmitry.molchanov.fishingforecast

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class FishWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        val isFishWorkerScheduled = context?.isWeatherDataWorkManagerScheduled() ?: return

        if (!isFishWorkerScheduled) {
            val work = OneTimeWorkRequestBuilder<UpdateWeatherDataWorkManager>()
                .setInitialDelay(DELAY_2H, TimeUnit.MILLISECONDS)
                .addTag(FISH_UPDATE_JOB_TAG)
                .build()
            WorkManager.getInstance(context).enqueue(work)
        }
    }
}