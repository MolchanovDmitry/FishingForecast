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
 * Тест экрана результатов рыбалки.
 *
 * Проверяет:
 * - Открытие вкладки Результаты
 * - Наличие кнопок действий
 * - Открытие диалога добавления результата
 */
@RunWith(AndroidJUnit4::class)
class ResultScreenTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun openResultsTab() = run {
        step("Открываем вкладку Результаты") {
            onView(withText("Результаты")).perform(click())
            onView(withText("Результаты")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun actionButtonsAreDisplayed() = run {
        step("Открываем вкладку Результаты") {
            onView(withText("Результаты")).perform(click())
        }

        step("Проверяем наличие кнопок действий") {
            onView(withText("Добавить новый результат.")).check(matches(isDisplayed()))
            onView(withText("Поделиться результатами.")).check(matches(isDisplayed()))
            onView(withText("Импортировать результаты.")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun openAddResultDialog() = run {
        step("Открываем вкладку Результаты") {
            onView(withText("Результаты")).perform(click())
        }

        step("Нажимаем 'Добавить новый результат.'") {
            onView(withText("Добавить новый результат.")).perform(click())
        }

        step("Диалог добавления результата открыт") {
            onView(withText("Введите данные для сохранения")).check(matches(isDisplayed()))
        }
    }
}
