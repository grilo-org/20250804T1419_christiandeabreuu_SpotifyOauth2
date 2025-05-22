package com.example.spotifyapi.app.ui.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotifyapi.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AppActivity::class.java)

    @Test
    fun testBottomNavMenuHasTopArtistsItem() {
        // Verifica se o item "Top Artists" está presente na BottomNavigationView
        onView(withText(R.string.top_artists)).check(matches(isDisplayed()))
    }

    @Test
    fun testBottomNavigationViewIsClickable() {
        // Verifica se a BottomNavigationView está habilitada para clique
        onView(withId(R.id.bottomNavigationView)).check(matches(isEnabled()))
    }

    @Test
    fun testClickTopArtistsBottomNavItem() {
        // Realiza clique no item Top Artists e espera que não ocorra erro
        onView(withText(R.string.top_artists)).perform(click())
    }

    @Test
    fun testBottomNavigationViewIsDisplayed() {
        // Verifica se a BottomNavigationView está visível na tela
        onView(withId(R.id.bottomNavigationView)).check(matches(isDisplayed()))
    }


}