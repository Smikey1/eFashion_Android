package com.hdd.globalmovie

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.hdd.globalmovie.presentation.activity.SignUpActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class RegisterUITest {

    @get:Rule
    val testRule = ActivityScenarioRule(SignUpActivity::class.java)

    @Test
    fun registerUITest(){
        onView(withId(R.id.et_sign_up_full_name))
            .perform(typeText("Admin Admin"))

        onView(withId(R.id.et_sign_up_email))
            .perform(typeText("admin@text.com"))

        onView(withId(R.id.et_sign_up_password))
            .perform(typeText("Admin12345"))

        onView(withId(R.id.et_sign_up_confirm_password))
            .perform(typeText("Admin12345"))

        closeSoftKeyboard()

        onView(withId(R.id.btnSignup))
            .perform(click())

        Thread.sleep(2500)
    }
}