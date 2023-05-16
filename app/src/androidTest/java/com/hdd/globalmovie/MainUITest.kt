package com.hdd.globalmovie

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.hdd.globalmovie.presentation.activity.SplashActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class MainUITest {

    @get:Rule
    val testRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun mainUITest(){
       onView(withId(R.id.btnNavigateToSignup)).perform(click())

        onView(withId(R.id.btnSkip)).perform(click())
        Thread.sleep(3000)

    }
}

