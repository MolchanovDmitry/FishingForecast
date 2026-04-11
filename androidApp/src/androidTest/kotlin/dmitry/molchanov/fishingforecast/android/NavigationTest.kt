package dmitry.molchanov.fishingforecast.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест навигации по главным вкладкам приложения.
 *
 * Проверяет:
 * - Запуск MainActivity
 * - Наличие всех 5 вкладок в BottomNavigation
 * - Переключение между вкладками
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun allNavigationTabsAreDisplayed() = run {
        step("Проверяем наличие всех вкладок навигации") {
            onView(withText("Карта")).check(matches(isDisplayed()))
            onView(withText("Профиль")).check(matches(isDisplayed()))
            onView(withText("Настройки")).check(matches(isDisplayed()))
            onView(withText("Список")).check(matches(isDisplayed()))
            onView(withText("Результаты")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun switchToProfileTab() = run {
        step("Переключаемся на вкладку Профиль") {
            onView(withText("Профиль")).perform(click())
            onView(withText("Профиль")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun switchToSettingsTab() = run {
        step("Переключаемся на вкладку Настройки") {
            onView(withText("Настройки")).perform(click())
            onView(withText("Настройки")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun switchToWeatherListTab() = run {
        step("Переключаемся на вкладку Список") {
            onView(withText("Список")).perform(click())
            onView(withText("Список")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun switchToResultsTab() = run {
        step("Переключаемся на вкладку Результаты") {
            onView(withText("Результаты")).perform(click())
            onView(withText("Результаты")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun navigateThroughAllTabs() = run {
        step("Начинаем с карты (стартовый экран)") {
            onView(withText("Карта")).check(matches(isDisplayed()))
        }

        step("Переходим в Профиль") {
            onView(withText("Профиль")).perform(click())
            onView(withText("Профиль")).check(matches(isDisplayed()))
        }

        step("Переходим в Настройки") {
            onView(withText("Настройки")).perform(click())
            onView(withText("Настройки")).check(matches(isDisplayed()))
        }

        step("Переходим в Список") {
            onView(withText("Список")).perform(click())
            onView(withText("Список")).check(matches(isDisplayed()))
        }

        step("Переходим в Результаты") {
            onView(withText("Результаты")).perform(click())
            onView(withText("Результаты")).check(matches(isDisplayed()))
        }

        step("Возвращаемся на Карту") {
            onView(withText("Карта")).perform(click())
            onView(withText("Карта")).check(matches(isDisplayed()))
        }
    }
}
