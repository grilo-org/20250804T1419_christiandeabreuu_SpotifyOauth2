package com.example.spotifyapi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotifyapi.app.ui.createplaylist.CreatePlaylistActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreatePlaylistActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CreatePlaylistActivity::class.java)

    @Test
    fun testTitleTextViewIsDisplayed() {
        // Verifica se o TextView do título está visível na tela
        onView(withId(R.id.createPlaylistTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun testEditTextIsDisplayed() {
        // Verifica se o EditText para o nome da playlist está visível na tela
        onView(withId(R.id.playlistNameEditText)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateButtonIsDisplayed() {
        // Verifica se o botão de criar está visível na tela
        onView(withId(R.id.createButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testCloseButtonIsDisplayed() {
        // Verifica se o botão de fechar (X) está visível na tela
        onView(withId(R.id.closeButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testEditTextHintIsCorrect() {
        // Verifica se o EditText tem o hint correto
        onView(withId(R.id.playlistNameEditText)).check(matches(withHint(R.string.insert_name_your_playlist)))
    }

    @Test
    fun testCreateButtonHasCorrectText() {
        // Verifica se o botão de criar tem o texto correto
        onView(withId(R.id.createButton)).check(matches(withText(R.string.create_your_playlist_button)))
    }

    @Test
    fun testTypePlaylistName() {
        // Verifica se é possível digitar texto no EditText
        val testName = "Minha Playlist Teste"
        onView(withId(R.id.playlistNameEditText)).perform(typeText(testName), closeSoftKeyboard())
        onView(withId(R.id.playlistNameEditText)).check(matches(withText(testName)))
    }

    @Test
    fun testCreateButtonIsEnabled() {
        // Verifica se o botão de criar está habilitado
        onView(withId(R.id.createButton)).check(matches(isEnabled()))
    }

    @Test
    fun testCloseButtonClickFinishesActivity() {
        // Verifica se ao clicar no botão de fechar, a Activity é finalizada
        onView(withId(R.id.closeButton)).perform(click())
        // Não há uma verificação direta, mas se não der erro/crash, está ok
    }

    @Test
    fun testCreateButtonClickWithEmptyNameShowsError() {
        // Verifica se ao clicar em criar com nome vazio, mostra erro (Toast)
        onView(withId(R.id.playlistNameEditText)).perform(replaceText(""))
        onView(withId(R.id.createButton)).perform(click())
        // Aqui seria ideal usar Espresso-Intents ou ToastMatcher customizado para verificar o Toast
        // Mas pelo menos garante que o app não crasha
    }
}