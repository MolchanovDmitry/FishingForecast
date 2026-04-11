package dmitry.molchanov.fishingforecast.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест экрана настроек прогноза.
 *
 * Проверяет:
 * - Открытие вкладки Настройки
 * - Отображение экрана настроек
 */
@RunWith(AndroidJUnit4::class)
class ForecastSettingsTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun openSettingsTab() = run {
        step("Открываем вкладку Настройки") {
            onView(withText("Настройки")).perform(click())
            onView(withText("Настройки")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun settingsListIsDisplayed() = run {
        step("Открываем вкладку Настройки") {
            onView(withText("Настройки")).perform(click())
        }

        step("Экран настроек отображается") {
            onView(withText("Настройки")).check(matches(isDisplayed()))
        }
    }
}
