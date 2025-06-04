package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.data.repository.UserProfileRepositoryImpl
import com.example.spotifyapi.auth.data.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UserProfileRepositoryTest {

    private lateinit var repository: UserProfileRepository
    private val apiService: SpotifyApiService = mockk(relaxed = true)
    private val spotifyDAO: SpotifyDAO = mockk(relaxed = true)
    private lateinit var tokenRepository: TokenRepository


    @Before
    fun setup() {
        repository = UserProfileRepositoryImpl(apiService, spotifyDAO, tokenRepository)
    }

    @Test
    fun `getUserProfileFromApi should return user profile when API call is successful`() =
        runBlocking {
            // Given
            val mockkListImages: List<Image> =
                listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
            val fakeProfile = UserProfile("1", "User Name", mockkListImages)
            coEvery { apiService.getUserProfile(any()) } returns fakeProfile

            // When
            val result = repository.getUserProfileFromApi()

            // Then - Verificando se o resultado é sucesso
            assertEquals(fakeProfile, result)
            coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
        }

    @Test
    fun `getUserProfileFromApi should return null when API call fails`() = runBlocking {
        // Given
        coEvery { apiService.getUserProfile(any()) } throws Exception("API Error")

        // When
        val result = repository.getUserProfileFromApi()

        // Then - Verificando se o resultado é nulo
        assertNull(result)
        coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
    }
}