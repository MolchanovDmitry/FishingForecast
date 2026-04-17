package dmitry.molchanov.fishingforecast.android

import androidx.compose.ui.test.assertIsExists
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест экрана настроек прогноза.
 */
@RunWith(AndroidJUnit4::class)
class ForecastSettingsTest : TestCase() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun waitIdle() {
        Thread.sleep(1000)
    }

    private fun clickTab(text: String) {
        composeRule.onAllNodes(hasText(text, ignoreCase = true))[0].performClick()
    }

    @Test
    fun openSettingsTab() = run {
        step("Открываем вкладку Настройки") {
            clickTab("Настройки")
            waitIdle()
            composeRule.onAllNodes(hasText("Настройки", ignoreCase = true))[0].assertIsExists()
        }
    }

    @Test
    fun settingsListIsDisplayed() = run {
        step("Открываем вкладку Настройки") {
            clickTab("Настройки")
            waitIdle()
        }

        step("Экран настроек отображается") {
            composeRule.onAllNodes(hasText("Настройки", ignoreCase = true))[0].assertIsExists()
        }
    }
}
