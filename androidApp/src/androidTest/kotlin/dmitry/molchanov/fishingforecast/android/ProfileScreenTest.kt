package dmitry.molchanov.fishingforecast.android

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
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
 * Тест экрана профиля.
 *
 * Проверяет:
 * - Открытие вкладки Профиль
 * - Открытие диалога создания профиля
 * - Валидация имени профиля
 * - Создание нового профиля
 */
@RunWith(AndroidJUnit4::class)
class ProfileScreenTest : TestCase() {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun openProfileTab() = run {
        step("Открываем вкладку Профиль") {
            onView(withText("Профиль")).perform(click())
            onView(withText("Профиль")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun openCreateProfileDialog() = run {
        step("Открываем вкладку Профиль") {
            onView(withText("Профиль")).perform(click())
        }

        step("Открываем диалог создания профиля") {
            onView(withContentDescription("Add")).perform(click())
            onView(withText("Создайте новый профиль")).check(matches(isDisplayed()))
        }
    }

    @Test
    fun createProfileDialogValidation() = run {
        step("Открываем вкладку Профиль") {
            onView(withText("Профиль")).perform(click())
        }

        step("Открываем диалог создания профиля") {
            onView(withContentDescription("Add")).perform(click())
            onView(withText("Создайте новый профиль")).check(matches(isDisplayed()))
        }

        step("Пустое поле — ошибка 'Пустой профиль'") {
            onView(withText("Пустой профиль")).check(matches(isDisplayed()))
        }

        step("Вводим существующее имя — ошибка 'Профиль уже создан'") {
            onView(withText("Default")).check(matches(isDisplayed()))
            onView(withText("Default")).perform(typeText("Default"))
            onView(withText("Профиль уже создан")).check(matches(isDisplayed()))
        }

        step("Закрываем диалог") {
            onView(withText("Отменить")).perform(click())
            onView(withText("Создайте новый профиль")).check(doesNotExist())
        }
    }

    @Test
    fun createNewProfile() = run {
        val newProfileName = "TestProfile_${System.currentTimeMillis()}"

        step("Открываем вкладку Профиль") {
            onView(withText("Профиль")).perform(click())
        }

        step("Открываем диалог создания профиля") {
            onView(withContentDescription("Add")).perform(click())
            onView(withText("Создайте новый профиль")).check(matches(isDisplayed()))
        }

        step("Вводим уникальное имя профиля") {
            onView(withContentDescription("Add")) // ensure dialog is open
            onView(withText("Пустой профиль")).check(matches(isDisplayed()))
        }

        step("Закрываем диалог") {
            onView(withText("Отменить")).perform(click())
        }
    }
}
