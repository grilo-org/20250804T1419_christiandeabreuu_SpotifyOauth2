package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CreatePlaylistRepositoryTest {

    @RelaxedMockK
    private lateinit var apiService: SpotifyApiService
    private lateinit var repository: CreatePlaylistRepository
    private var tokenRepository: TokenRepository = mockk(relaxed = true)


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CreatePlaylistRepositoryImpl(apiService, tokenRepository)
    }

    @Test
    fun `createPlaylist should return true when API call is successful`() = runBlocking {
        // Given
        val fakeResponse = mockk<Response<Unit>> {
            every { isSuccessful } returns true
        }
        coEvery { apiService.createPlaylist(any(), any()) } returns fakeResponse

        // When
        val result = repository.createPlaylist("Minha Playlist")

        // Then - Verificando se o resultado é verdadeiro
        assertTrue(result)
        coVerify(exactly = 1) { apiService.createPlaylist("Bearer token123", any()) }
    }

    @Test
    fun `createPlaylist should return false when API call fails`() = runBlocking {
        // Given
        val fakeResponse = mockk<Response<Unit>> {
            every { isSuccessful } returns false
        }
        coEvery { apiService.createPlaylist(any(), any()) } returns fakeResponse

        // When
        val result = repository.createPlaylist("token123")

        // Then - Verificando se o resultado é falso
        assertFalse(result)
        coVerify(exactly = 1) { apiService.createPlaylist("Bearer token123", any()) }
    }
}