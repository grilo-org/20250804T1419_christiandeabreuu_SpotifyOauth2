package com.example.spotifyapi

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotifyapi.app.ui.app.AppActivity
import com.example.spotifyapi.app.ui.topartists.TopArtistsFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopArtistsFragmentTest {


    @Test
    fun testTopArtistsTitleIsDisplayed() {
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.topArtistasTextView))
            .check(matches(isDisplayed()))
    }
    // Verifica se o título "Top Artistas" está visível.


    @Test
    fun testRecyclerViewIsDisplayed() {
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.artistasRecyclerView))
            .check(matches(isDisplayed()))
    }
    // Verifica se a lista de artistas (RecyclerView) está visível.



    @Test
    fun testProfileImageIsDisplayed() {
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.profileImageView))
            .check(matches(isDisplayed()))
    }
    // Verifica se a imagem de perfil está visível.


    @Test
    fun testTitleHasCorrectText() {
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.topArtistasTextView))
            .check(matches(withText(R.string.top_artists)))
    }
    // Verifica se o título está correto de acordo com o arquivo de strings.


    @Test
    fun testRecyclerViewIsEnabled() {
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.artistasRecyclerView))
            .check(matches(isEnabled()))
    }
    // Verifica se a lista de artistas está habilitada.

}

@RunWith(AndroidJUnit4::class)
class TopArtistsFragmentInAppActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(AppActivity::class.java)

    @Test
    fun testFragmentViewsAreDisplayed() {
        onView(withId(R.id.topArtistasTextView)).check(matches(isDisplayed()))
    }
}