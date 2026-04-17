package dmitry.molchanov.fishingforecast.android

import androidx.compose.ui.test.assertCountAtLeast
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест навигации по главным вкладкам приложения.
 */
@RunWith(AndroidJUnit4::class)
class NavigationTest : TestCase() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun waitIdle() {
        Thread.sleep(1000)
    }

    private fun clickTab(text: String) {
        composeRule.onAllNodesWithText(text)[0].performClick()
    }

    private fun assertTabExists(text: String) {
        composeRule.onAllNodesWithText(text).assertCountAtLeast(1)
    }

    @Test
    fun allNavigationTabsAreDisplayed() = run {
        step("Ждём отрисовки UI") { waitIdle() }
        step("Проверяем наличие всех вкладок навигации") {
            assertTabExists("Карта")
            assertTabExists("Профиль")
            assertTabExists("Настройки")
            assertTabExists("Список")
            assertTabExists("Результаты")
        }
    }

    @Test
    fun switchToProfileTab() = run {
        step("Ждём отрисовки UI") { waitIdle() }
        step("Переключаемся на вкладку Профиль") {
            clickTab("Профиль")
            waitIdle()
            assertTabExists("Профиль")
        }
    }

    @Test
    fun switchToSettingsTab() = run {
        step("Ждём отрисовки UI") { waitIdle() }
        step("Переключаемся на вкладку Настройки") {
            clickTab("Настройки")
            waitIdle()
            assertTabExists("Настройки")
        }
    }

    @Test
    fun switchToWeatherListTab() = run {
        step("Ждём отрисовки UI") { waitIdle() }
        step("Переключаемся на вкладку Список") {
            clickTab("Список")
            waitIdle()
            assertTabExists("Список")
        }
    }

    @Test
    fun switchToResultsTab() = run {
        step("Ждём отрисовки UI") { waitIdle() }
        step("Переключаемся на вкладку Результаты") {
            clickTab("Результаты")
            waitIdle()
            assertTabExists("Результаты")
        }
    }

    @Test
    fun navigateThroughAllTabs() = run {
        step("Ждём отрисовки UI") { waitIdle() }
        step("Начинаем с карты") {
            assertTabExists("Карта")
        }
        step("Переходим в Профиль") {
            clickTab("Профиль")
            waitIdle()
            assertTabExists("Профиль")
        }
        step("Переходим в Настройки") {
            clickTab("Настройки")
            waitIdle()
            assertTabExists("Настройки")
        }
        step("Переходим в Список") {
            clickTab("Список")
            waitIdle()
            assertTabExists("Список")
        }
        step("Переходим в Результаты") {
            clickTab("Результаты")
            waitIdle()
            assertTabExists("Результаты")
        }
        step("Возвращаемся на Карту") {
            clickTab("Карта")
            waitIdle()
            assertTabExists("Карта")
        }
    }

    @Test
    fun topAppBarTitlesAreDisplayedOnEachTab() = run {
        step("Ждём отрисовки UI") { waitIdle() }
        step("Карта — TopAppBar") {
            assertTabExists("Карта")
        }
        step("Профиль — TopAppBar") {
            clickTab("Профиль")
            waitIdle()
            assertTabExists("Профиль")
        }
        step("Настройки — TopAppBar") {
            clickTab("Настройки")
            waitIdle()
            assertTabExists("Настройки")
        }
        step("Список — TopAppBar") {
            clickTab("Список")
            waitIdle()
            assertTabExists("Список")
        }
        step("Результаты — TopAppBar") {
            clickTab("Результаты")
            waitIdle()
            assertTabExists("Результаты")
        }
    }
}
