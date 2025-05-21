package com.example.spotifyapi.authenticate.domain.usecase

import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import com.example.spotifyapi.authenticate.data.repository.AuthRepository
import com.example.spotifyapi.authenticate.domain.usecase.GetAccessTokenUseCase
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetAccessTokenUseCaseTest {

    private lateinit var getAccessTokenUseCase: GetAccessTokenUseCase
    private val repository: AuthRepository = mockk()

    @Before
    fun setup() {
        getAccessTokenUseCase = GetAccessTokenUseCase(repository)
    }

    // 🔹 Testando sucesso na obtenção do token
    @Test
    fun `execute should return SpotifyTokens when API call is successful`() = runBlocking {
        val fakeTokens = SpotifyTokens("access123", "refresh456")

        coEvery { repository.getAccessToken(any(), any()) } returns Result.success(fakeTokens)

        val result = getAccessTokenUseCase.execute("authCode", "redirectUri")

        assertTrue(result.isSuccess)
        assertEquals(fakeTokens, result.getOrNull())

        coVerify(exactly = 1) { repository.getAccessToken("authCode", "redirectUri") }
    }

    // 🔹 Testando erro ao chamar `getAccessToken()`
    @Test
    fun `execute should return failure when API call fails`() = runBlocking {
        coEvery { repository.getAccessToken(any(), any()) } returns Result.failure(Exception("Erro na API"))

        val result = getAccessTokenUseCase.execute("authCode", "redirectUri")

        assertTrue(result.isFailure)
        assertEquals("Erro na API", result.exceptionOrNull()?.message)

        coVerify(exactly = 1) { repository.getAccessToken("authCode", "redirectUri") }
    }
}