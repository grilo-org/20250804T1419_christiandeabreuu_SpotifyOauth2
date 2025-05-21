package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.model.Image
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.model.UserProfile

class UserProfileRepositoryTest {

    private lateinit var repository: UserProfileRepository
    private val apiService: SpotifyApiService = mockk(relaxed = true)
    private val spotifyDAO: SpotifyDAO = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = UserProfileRepository(apiService, spotifyDAO)
    }

    @Test
    fun `getUserProfileFromApi should return user profile when API call is successful`() = runBlocking {
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val fakeProfile = UserProfile("1", "User Name", mockkListImages)

        coEvery { apiService.getUserProfile(any()) } returns fakeProfile

        val result = repository.getUserProfileFromApi("token123")

        assertEquals(fakeProfile, result)
        coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
    }

    @Test
    fun `getUserProfileFromApi should return null when API call fails`() = runBlocking {
        coEvery { apiService.getUserProfile(any()) } throws Exception("API Error")

        val result = repository.getUserProfileFromApi("token123")

        assertNull(result)
        coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
    }
}