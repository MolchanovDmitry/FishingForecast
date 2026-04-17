package dmitry.molchanov.fishingforecast.android

import android.os.Bundle
import androidx.test.runner.AndroidJUnitRunner

/**
 * Custom test runner to properly initialize the Application during tests.
 */
class FishingForecastTestRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle) {
        arguments.putString("disableAnalytics", "true")
        super.onCreate(arguments)
    }

    override fun newApplication(cl: ClassLoader?, className: String?, context: android.content.Context?): android.app.Application {
        return super.newApplication(cl, App::class.java.name, context)
    }
}
