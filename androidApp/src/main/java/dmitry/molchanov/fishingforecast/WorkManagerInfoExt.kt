package dmitry.molchanov.fishingforecast

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager

fun Context.getWeatherDataUpdateWorkInfos(): List<WorkInfo> {
    val workManager = WorkManager.getInstance(this)
    val workInfos = workManager.getWorkInfosByTag(FISH_UPDATE_JOB_TAG).get()
    return workInfos
}

fun Context.isWeatherDataWorkManagerScheduled(): Boolean {
    val workInfos = getWeatherDataUpdateWorkInfos()

    val isFishWorkerScheduled = workInfos
        .map { workInfo ->
            val isWorkScheduled = when (workInfo.state) {
                WorkInfo.State.ENQUEUED,
                WorkInfo.State.RUNNING,
                WorkInfo.State.BLOCKED -> true

                WorkInfo.State.FAILED,
                WorkInfo.State.SUCCEEDED,
                WorkInfo.State.CANCELLED -> false
            }
            isWorkScheduled
        }.firstOrNull()
        ?: false

    return isFishWorkerScheduled
}

const val FISH_UPDATE_JOB_TAG = "FISH_UPDATE_JOB_TAG"
const val DELAY_2H = 7700000L