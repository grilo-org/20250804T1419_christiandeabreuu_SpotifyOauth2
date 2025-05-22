package com.example.spotifyapi

import com.example.spotifyapi.app.ui.topartists.TopArtistsFragment

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import com.example.spotifyapi.R
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TopArtistFragTest {

    @Test
    fun testRecyclerViewIsVisible() {
        // Lança o fragmento dentro de um contêiner simulado pelo Robolectric
        val scenario = launchFragmentInContainer<TopArtistsFragment>(
            fragmentArgs = Bundle(),
            themeResId = R.style.Theme_SpotifyApi // Use o tema do seu app, se necessário
        )

        scenario.onFragment { fragment ->
            val recyclerView =
                fragment.view?.findViewById<RecyclerView>(R.id.artistasRecyclerView)
            assertNotNull("RecyclerView deve estar presente no layout", recyclerView)
        }
    }
}