package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import io.mockk.Awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
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

    @Before
    fun setup() {
        repository = UserProfileRepository(apiService, spotifyDAO)
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
            val result = repository.getUserProfileFromApi("token123")

            // Then - Verificando se o resultado corresponde ao esperado
            assertEquals(fakeProfile, result)
            coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
        }

    @Test
    fun `getUserProfileFromApi should return null when API call fails`() = runBlocking {
        // Given
        coEvery { apiService.getUserProfile(any()) } throws Exception("API Error")

        //When
        val result = repository.getUserProfileFromApi("token123")

        // Then - Verificando se o resultado é nulo
        assertNull(result)
        coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
    }

    @Test
    fun `insertLocalUserProfile should call DAO to save user profile`() = runBlocking {
        // Given
        val fakeProfileDB = UserProfileDB(0, "id", "User Name", "imageUrl")
        coEvery { spotifyDAO.insertLocalUserProfile(fakeProfileDB) } just Awaits

        // When
        repository.insertLocalUserProfile(fakeProfileDB)

        // Then - Teste para salvar um perfil localmente
        coVerify(exactly = 1) { spotifyDAO.insertLocalUserProfile(fakeProfileDB) }
    }

    @Test
    fun `getLocalUserProfile should return user profile from local database`() = runBlocking {
        // Given
        val fakeProfileDB = UserProfileDB(0, "id", "User Name", "imageUrl")

        coEvery { spotifyDAO.getLocalUserProfile() } returns fakeProfileDB

        // When
        val result = repository.getLocalUserProfile()

        // Then - Teste para recuperar um perfil local salvo é recuperado corretamente
        assertEquals(fakeProfileDB, result)
        coVerify(exactly = 1) { spotifyDAO.getLocalUserProfile() }
    }

    @Test
    fun `getLocalUserProfile should return null when no profile is found`() = runBlocking {
        // Given
        coEvery { spotifyDAO.getLocalUserProfile() } returns null

        // When
        val result = repository.getLocalUserProfile()

        // Then - Teste para verificar retorno null quando não há perfil local
        assertNull(result)
        coVerify(exactly = 1) { spotifyDAO.getLocalUserProfile() }
    }
}