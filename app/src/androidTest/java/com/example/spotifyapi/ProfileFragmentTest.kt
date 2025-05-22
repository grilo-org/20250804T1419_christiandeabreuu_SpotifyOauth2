package com.example.spotifyapi.app.ui.profile

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotifyapi.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    @Test
    fun testProfileImageViewIsDisplayed() {
        // Verifica se a imagem de perfil está visível
        launchFragmentInContainer<ProfileFragment>(themeResId = R.style.SplashTheme)
        launchFragmentInContainer<ProfileFragment>()
        onView(withId(R.id.profileImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testProfileTextViewIsDisplayed() {
        // Verifica se o TextView do nome do perfil está visível
        launchFragmentInContainer<ProfileFragment>()
        onView(withId(R.id.profileTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun testCloseButtonIsDisplayed() {
        // Verifica se o botão de fechar está visível
        launchFragmentInContainer<ProfileFragment>()
        onView(withId(R.id.buttonClose)).check(matches(isDisplayed()))
    }

    @Test
    fun testProfileTextViewHasCorrectHintText() {
        // Verifica se o TextView tem o texto correto definido no arquivo de strings
        launchFragmentInContainer<ProfileFragment>()
        onView(withId(R.id.profileTextView)).check(matches(withText(R.string.name_profile_spotify)))
    }

    @Test
    fun testButtonCloseHasCorrectText() {
        // Verifica se o botão de fechar tem o texto correto
        launchFragmentInContainer<ProfileFragment>()
        onView(withId(R.id.buttonClose)).check(matches(withText(R.string.close_app_button)))
    }

    @Test
    fun testProfileImageViewHasCorrectContentDescription() {
        // Verifica se a imagem de perfil tem a contentDescription correta
        launchFragmentInContainer<ProfileFragment>()
        onView(withId(R.id.profileImageView)).check(matches(withContentDescription(R.string.image_profile)))
    }
}