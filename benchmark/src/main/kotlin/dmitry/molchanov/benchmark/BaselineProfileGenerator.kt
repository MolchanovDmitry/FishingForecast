package dmitry.molchanov.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест для генерации Baseline Profile.
 *
 * Запускается на Gradle Managed Device (Pixel 6 API 34) командой:
 * ```
 * ./gradlew :benchmark:generateBaselineProfile
 * ```
 *
 * Сценарий: холодный старт приложения → ожидание загрузки первого экрана с картой.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfile() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val packageName = context.packageName

        baselineProfileRule.collect(packageName) {
            // Возвращаемся на домашний экран для холодного старта
            pressHome()

            // Запускаем приложение через launch intent
            startActivityAndWait()

            // Ждём, пока загрузится первый экран с картой
            waitForMapScreen()
        }
    }
}

/**
 * Ждём появления ключевых элементов первого экрана (карта).
 * Для Яндекс Карт проверяем наличие MapView.
 */
fun androidx.benchmark.macro.MacrobenchmarkScope.waitForMapScreen() {
    // Ждём появления Yandex MapView
    val mapFound = device.wait(
        Until.hasObject(By.clazz("com.yandex.mapkit.mapview.MapView")),
        15_000L
    )

    if (!mapFound) {
        // Fallback: ждём появления текста "Карта" (название вкладки)
        device.wait(
            Until.hasObject(By.text("Карта")),
            5_000L
        )
    }

    // Даём время на полную отрисовку
    Thread.sleep(1_000)
}
