package com.example.spotifyapi

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotifyapi.app.ui.topartists.TopArtistsFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopArtistsFragmentTest {

    @Test
    fun testTopArtistsTitleIsDisplayed() {
        // Verifica se o título "Top Artistas" está visível.
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.topArtistasTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewIsDisplayed() {
        // Verifica se a lista de artistas (RecyclerView) está visível.
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.artistasRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testProfileImageIsDisplayed() {
        // Verifica se a imagem de perfil está visível.
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.profileImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testTitleHasCorrectText() {
        // Verifica se o título está correto de acordo com o arquivo de strings.
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.topArtistasTextView)).check(matches(withText(R.string.top_artists)))
    }

    @Test
    fun testRecyclerViewIsEnabled() {
        // Verifica se a lista de artistas está habilitada.
        launchFragmentInContainer<TopArtistsFragment>()
        onView(withId(R.id.artistasRecyclerView)).check(matches(isEnabled()))
    }
}