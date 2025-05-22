package com.example.spotifyapi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spotifyapi.app.ui.app.AppActivity
import com.example.spotifyapi.authenticate.ui.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun testTextViewIsDisplayed() {
        onView(withId(R.id.tvEnterYourAccount))
            .check(matches(isDisplayed()))
    }
    //Verifica se o TextView de instrução para entrar na conta está visível na tela.

    @Test
    fun testButtonIsDisplayed() {
        onView(withId(R.id.buttonStart))
            .check(matches(isDisplayed()))
    }
    //Verifica se o botão "Entrar" está visível na tela.

    @Test
    fun testButtonCanBeClicked() {
        onView(withId(R.id.buttonStart))
            .perform(click())
    }
    //Verifica se o botão "Entrar" pode ser clicado.

    @Test
    fun testButtonHasCorrectText() {
        onView(withId(R.id.buttonStart))
            .check(matches(withText(R.string.enter_button)))
    }
    // Verifica se o texto do botão "Entrar" está correto conforme o esperado no arquivo de strings.

    @Test
    fun testTextViewHasCorrectText() {
        onView(withId(R.id.tvEnterYourAccount))
            .check(matches(withText("Entre com sua conta Spotify")))
    }
    // Verifica se o TextView de instrução tem o texto correto conforme o arquivo de strings.

    @Test
    fun testButtonIsEnabled() {
        onView(withId(R.id.buttonStart))
            .check(matches(isEnabled()))
    }
    // Verifica se o botão "Entrar" está habilitado para clique.

    @Test
    fun testComponentsAreDisplayedTogether() {
        onView(withId(R.id.tvEnterYourAccount)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.buttonStart)).check(matches(isCompletelyDisplayed()))
    }
    // Verifica se os componentes estão corretamente posicionados na tela
}