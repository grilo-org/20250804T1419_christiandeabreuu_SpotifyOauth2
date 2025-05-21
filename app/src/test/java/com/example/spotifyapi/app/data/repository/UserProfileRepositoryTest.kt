package com.example.spotifyapi.app.data.repository

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.Image

class UserProfileRepositoryTest {

    private lateinit var repository: UserProfileRepository
    private val apiService: SpotifyApiService = mockk(relaxed = true)
    private val spotifyDAO: SpotifyDAO = mockk(relaxed = true)

    @Before
    fun setup() {
        repository = UserProfileRepository(apiService, spotifyDAO)
    }

    // 🔹 Teste de chamada bem-sucedida da API
    @Test
    fun `getUserProfileFromApi should return user profile when API call is successful`() = runBlocking {
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val fakeProfile = UserProfile("1", "User Name", mockkListImages)

        coEvery { apiService.getUserProfile(any()) } returns fakeProfile

        val result = repository.getUserProfileFromApi("token123")

        assertEquals(fakeProfile, result)
        coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
    } //Testamos getUserProfileFromApi() → Retorno correto quando a API responde e quando falha.


    // 🔹 Teste quando a chamada da API falha
    @Test
    fun `getUserProfileFromApi should return null when API call fails`() = runBlocking {
        coEvery { apiService.getUserProfile(any()) } throws Exception("API Error")

        val result = repository.getUserProfileFromApi("token123")

        assertNull(result)
        coVerify(exactly = 1) { apiService.getUserProfile("Bearer token123") }
    }// Testamos getUserProfileFromApi() → Retorno correto quando a API responde e quando falha.


    // 🔹 Teste para salvar um perfil localmente
    @Test
    fun `insertLocalUserProfile should call DAO to save user profile`() = runBlocking {
        val fakeProfileDB = UserProfileDB(0, "id", "User Name", "imageUrl")

        coEvery { spotifyDAO.insertLocalUserProfile(fakeProfileDB) } just Awaits

        repository.insertLocalUserProfile(fakeProfileDB)

        coVerify(exactly = 1) { spotifyDAO.insertLocalUserProfile(fakeProfileDB) }
    } // Testamos insertLocalUserProfile() → Garantimos que a DAO foi chamada para salvar os dados.



    // 🔹 Teste para recuperar um perfil local salvo
    @Test
    fun `getLocalUserProfile should return user profile from local database`() = runBlocking {
        val fakeProfileDB = UserProfileDB(0, "id", "User Name", "imageUrl")

        coEvery { spotifyDAO.getLocalUserProfile() } returns fakeProfileDB

        val result = repository.getLocalUserProfile()

        assertEquals(fakeProfileDB, result)
        coVerify(exactly = 1) { spotifyDAO.getLocalUserProfile() }
    } // Testamos getLocalUserProfile() → Verificamos se o perfil local é recuperado corretamente e também testamos quando ele não existe.


    // 🔹 Teste para verificar retorno null quando não há perfil local
    @Test
    fun `getLocalUserProfile should return null when no profile is found`() = runBlocking {
        coEvery { spotifyDAO.getLocalUserProfile() } returns null

        val result = repository.getLocalUserProfile()

        assertNull(result)
        coVerify(exactly = 1) { spotifyDAO.getLocalUserProfile() }
    } // Testamos getLocalUserProfile() → Verificamos se o perfil local é recuperado corretamente e também testamos quando ele não existe.

}