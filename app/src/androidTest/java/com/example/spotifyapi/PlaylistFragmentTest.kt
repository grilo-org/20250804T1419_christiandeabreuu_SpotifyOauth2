package com.example.spotifyapi


import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.spotifyapi.app.ui.playlist.PlaylistFragment
import org.junit.Test

class PlaylistFragmentTest {

    @Test
    fun testPlaylistsTextViewIsDisplayed() {
        // Verifica se o título "My Playlists" está visível
        launchFragmentInContainer<PlaylistFragment>(themeResId = R.style.SplashTheme)
        onView(withId(R.id.playlistsTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun testNoPlaylistsTextViewIsGoneInitially() {
        // Verifica se a mensagem "No Playlist Available" está oculta inicialmente
        launchFragmentInContainer<PlaylistFragment>(themeResId = R.style.SplashTheme)
        onView(withId(R.id.noPlaylistsTextView)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun testPlaylistsProfileImageViewIsDisplayed() {
        // Verifica se a imagem de perfil está visível
        launchFragmentInContainer<PlaylistFragment>(themeResId = R.style.SplashTheme)
        onView(withId(R.id.playlistsProfileImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun testPlaylistsRecyclerViewIsDisplayed() {
        // Verifica se o RecyclerView das playlists está visível
        launchFragmentInContainer<PlaylistFragment>(themeResId = R.style.SplashTheme)
        onView(withId(R.id.playlistsRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreatePlaylistButtonIsDisplayed() {
        // Verifica se o botão de criar playlist está visível
        launchFragmentInContainer<PlaylistFragment>(themeResId = R.style.SplashTheme)
        onView(withId(R.id.buttonToGoCreatePlaylist)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreatePlaylistButtonHasCorrectText() {
        // Verifica se o botão de criar playlist tem o texto correto
        launchFragmentInContainer<PlaylistFragment>(themeResId = R.style.SplashTheme)
        onView(withId(R.id.buttonToGoCreatePlaylist)).check(matches(withText(R.string.create_playlist_button)))
    }
}