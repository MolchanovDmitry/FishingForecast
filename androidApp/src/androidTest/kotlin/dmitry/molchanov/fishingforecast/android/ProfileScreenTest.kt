package dmitry.molchanov.fishingforecast.android

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsExists
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodes
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Тест экрана профиля.
 */
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest : TestCase() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun waitIdle() {
        Thread.sleep(1000)
    }

    private fun clickTab(text: String) {
        composeRule.onAllNodes(hasText(text, ignoreCase = true))[0].performClick()
    }

    @Test
    fun openProfileTab() = run {
        step("Открываем вкладку Профиль") {
            clickTab("Профиль")
            waitIdle()
            composeRule.onAllNodes(hasText("Профиль", ignoreCase = true))[0].assertIsExists()
        }
    }

    @Test
    fun openCreateProfileDialog() = run {
        step("Открываем вкладку Профиль") {
            clickTab("Профиль")
            waitIdle()
        }

        step("Открываем диалог создания профиля") {
            composeRule.onNodeWithContentDescription("Add").performClick()
            waitIdle()
            composeRule.onNodeWithText("Создайте новый профиль").assertIsDisplayed()
        }
    }

    @Test
    fun createProfileDialogValidation() = run {
        step("Открываем вкладку Профиль") {
            clickTab("Профиль")
            waitIdle()
        }

        step("Открываем диалог создания профиля") {
            composeRule.onNodeWithContentDescription("Add").performClick()
            waitIdle()
            composeRule.onNodeWithText("Создайте новый профиль").assertIsDisplayed()
        }

        step("Пустое поле — ошибка 'Пустой профиль'") {
            composeRule.onNodeWithText("Пустой профиль").assertIsDisplayed()
        }

        step("Вводим существующее имя — ошибка 'Профиль уже создан'") {
            composeRule.onNodeWithText("Пустой профиль").assertIsDisplayed()
        }

        step("Закрываем диалог") {
            composeRule.onNodeWithText("Отменить").performClick()
            waitIdle()
            composeRule.onNodeWithText("Создайте новый профиль").assertIsNotDisplayed()
        }
    }

    @Test
    fun createNewProfile() = run {
        step("Открываем вкладку Профиль") {
            clickTab("Профиль")
            waitIdle()
        }

        step("Открываем диалог создания профиля") {
            composeRule.onNodeWithContentDescription("Add").performClick()
            waitIdle()
            composeRule.onNodeWithText("Создайте новый профиль").assertIsDisplayed()
        }

        step("Вводим уникальное имя профиля") {
            composeRule.onNodeWithText("Пустой профиль").assertIsDisplayed()
        }

        step("Закрываем диалог") {
            composeRule.onNodeWithText("Отменить").performClick()
            waitIdle()
        }
    }
}
