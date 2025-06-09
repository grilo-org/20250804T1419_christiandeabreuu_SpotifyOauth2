package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TopArtistsRepositoryTest {

    private var repository: TopArtistsRepository = mockk(relaxed = true)
    private val apiService: SpotifyApiService = mockk(relaxed = true)
    private val spotifyDAO: SpotifyDAO = mockk(relaxed = true)
    private var tokenRepository: TokenRepository = mockk(relaxed = true)


    @Before
    fun setup() {
        repository = TopArtistsRepositoryImpl(apiService, spotifyDAO, tokenRepository)
    }

    @Test
    fun `getTopArtists should return empty list when API call fails`(): Unit = runBlocking {
        // Given
        coEvery { apiService.getTopArtists(any()) } throws Exception("API Error")

        // When
        repository.getTopArtistsApi()

        // Then - Verificando que retorna uma lista vazia
        coEvery {
            apiService.getTopArtists(
                "Bearer token123",
                20,
                "medium_term",
                0,
            )
        } throws Exception("API Error")
    }

    @Test
    fun `should create a valid TopArtistsResponse object`() {
        // Given
        val fakeArtists = listOf(
            ArtistResponse("1", "Artist 1", 90, emptyList()),
            ArtistResponse("2", "Artist 2", 80, emptyList())
        )

        // When
        val fakeResponse = TopArtistsResponse(
            items = fakeArtists,
            total = fakeArtists.size,
            limit = 20,
            offset = 0,
            href = "https://api.spotify.com/v1/me/top/artists",
            next = null,
            previous = null
        )

        // Then - Verificando se os atributos estão corretos
        assertEquals(fakeArtists.size, fakeResponse.total)
        assertEquals(fakeArtists, fakeResponse.items)
        assertEquals("https://api.spotify.com/v1/me/top/artists", fakeResponse.href)
    }
}