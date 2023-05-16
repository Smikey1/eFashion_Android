package com.hdd.globalmovie

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.hdd.globalmovie.presentation.activity.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class LoginUITest {

    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun loginUITest(){
        onView(withId(R.id.et_sign_in_email))
            .perform(typeText("hello@text.com"))

        onView(withId(R.id.et_sign_in_password))
            .perform(typeText("hello123"))

        closeSoftKeyboard()

        onView(withId(R.id.btnLogin))
            .perform(click())

        Thread.sleep(2000)

    }
}

