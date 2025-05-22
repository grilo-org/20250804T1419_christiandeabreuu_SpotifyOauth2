package com.example.spotifyapi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
        //Verifica se o TextView de instrução para entrar na conta está visível na tela.
        onView(withId(R.id.tvEnterYourAccount)).check(matches(isDisplayed()))
    }

    @Test
    fun testButtonIsDisplayed() {
        //Verifica se o botão "Entrar" está visível na tela.
        onView(withId(R.id.buttonStart)).check(matches(isDisplayed()))
    }

    @Test
    fun testButtonCanBeClicked() {
        //Verifica se o botão "Entrar" pode ser clicado.
        onView(withId(R.id.buttonStart)).perform(click())
    }

    @Test
    fun testButtonHasCorrectText() {
        // Verifica se o texto do botão "Entrar" está correto conforme o esperado no arquivo de strings.
        onView(withId(R.id.buttonStart)).check(matches(withText(R.string.enter_button)))
    }

    @Test
    fun testTextViewHasCorrectText() {
        // Verifica se o TextView de instrução tem o texto correto conforme o arquivo de strings.
        onView(withId(R.id.tvEnterYourAccount)).check(matches(withText("Entre com sua conta Spotify")))
    }

    @Test
    fun testButtonIsEnabled() {
        // Verifica se o botão "Entrar" está habilitado para clique.
        onView(withId(R.id.buttonStart)).check(matches(isEnabled()))
    }

    @Test
    fun testComponentsAreDisplayedTogether() {
        // Verifica se os componentes estão corretamente posicionados na tela
        onView(withId(R.id.tvEnterYourAccount)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.buttonStart)).check(matches(isCompletelyDisplayed()))
    }
}