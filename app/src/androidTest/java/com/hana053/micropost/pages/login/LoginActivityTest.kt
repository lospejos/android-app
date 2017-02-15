package com.hana053.micropost.pages.login

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.hana053.micropost.R
import com.hana053.micropost.services.LoginService
import com.hana053.micropost.testing.InjectableTest
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rx.Observable


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest : InjectableTest {

    @Rule @JvmField
    val activityRule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Test
    fun shouldBeOpened() {
        activityRule.launchActivity(null)
        onView(withText(R.string.log_in_to_micropost)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisableOrEnableBtn() {
        activityRule.launchActivity(null)

        val loginBtn = withId(R.id.loginBtn)
        val emailEditText = withId(R.id.emailEditText)
        val passwordEditText = withId(R.id.passwordEditText)

        onView(loginBtn).check(ViewAssertions.matches(not(isEnabled())))

        onView(emailEditText).perform(typeText("test@test.com"))
        onView(passwordEditText).perform(typeText("secret123"), closeSoftKeyboard())
        onView(loginBtn).check(ViewAssertions.matches(isEnabled()))

        onView(passwordEditText).perform(clearText(), closeSoftKeyboard())
        onView(loginBtn).check(ViewAssertions.matches(not(isEnabled())))

        onView(emailEditText).perform(clearText())
        onView(passwordEditText).perform(typeText("secret123"), closeSoftKeyboard())
        onView(loginBtn).check(ViewAssertions.matches(not(isEnabled())))
    }

    @Test
    fun shouldNavigateToMainWhenEmailAndPasswordIsValid() {
        overrideAppBindings {
            bind<LoginService>(overrides = true) with instance(mock<LoginService> {
                on { login(any(), any()) } doReturn Observable.just<Void>(null)
            })
        }

        activityRule.launchActivity(null)

        val loginBtn = withId(R.id.loginBtn)
        val emailEditText = withId(R.id.emailEditText)
        val passwordEditText = withId(R.id.passwordEditText)

        onView(emailEditText).perform(typeText("test@test.com"))
        onView(passwordEditText).perform(typeText("secret123"), closeSoftKeyboard())
        onView(loginBtn).perform(click())

        onView(withText(R.string.home)).check(matches(isDisplayed()))
    }
}