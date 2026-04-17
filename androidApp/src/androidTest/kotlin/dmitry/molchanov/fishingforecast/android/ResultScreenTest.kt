package dmitry.molchanov.fishingforecast.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsExists
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест экрана результатов рыбалки.
 */
@RunWith(AndroidJUnit4::class)
class ResultScreenTest : TestCase() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun waitIdle() {
        Thread.sleep(1000)
    }

    private fun clickTab(text: String) {
        composeRule.onAllNodes(hasText(text, ignoreCase = true))[0].performClick()
    }

    @Test
    fun openResultsTab() = run {
        step("Открываем вкладку Результаты") {
            clickTab("Результаты")
            waitIdle()
            composeRule.onAllNodes(hasText("Результаты", ignoreCase = true))[0].assertIsExists()
        }
    }

    @Test
    fun topAppBarTitleIsDisplayed() = run {
        step("Открываем вкладку Результаты") {
            clickTab("Результаты")
            waitIdle()
        }

        step("Проверяем заголовок TopAppBar") {
            composeRule.onAllNodes(hasText("Результаты", ignoreCase = true))[0].assertIsExists()
        }
    }

    @Test
    fun actionButtonsAreDisplayed() = run {
        step("Открываем вкладку Результаты") {
            clickTab("Результаты")
            waitIdle()
        }

        step("Проверяем наличие кнопок действий") {
            composeRule.onNodeWithText("Добавить новый результат.").assertIsDisplayed()
            composeRule.onNodeWithText("Поделиться результатами.").assertIsDisplayed()
            composeRule.onNodeWithText("Импортировать результаты.").assertIsDisplayed()
        }
    }

    @Test
    fun openAddResultDialog() = run {
        step("Открываем вкладку Результаты") {
            clickTab("Результаты")
            waitIdle()
        }

        step("Нажимаем 'Добавить новый результат.'") {
            composeRule.onNodeWithText("Добавить новый результат.").performClick()
            waitIdle()
        }

        step("Диалог добавления результата открыт") {
            composeRule.onNodeWithText("Введите данные для сохранения").assertIsDisplayed()
        }
    }
}
