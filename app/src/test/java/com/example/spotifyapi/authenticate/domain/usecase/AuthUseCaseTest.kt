package com.example.spotifyapi.authenticate.domain.usecase

import com.example.spotifyapi.authenticate.data.model.SpotifyTokens
import com.example.spotifyapi.authenticate.data.repository.TokenRepository
import com.example.spotifyapi.authenticate.domain.usecase.AuthUseCase
import com.example.spotifyapi.authenticate.domain.usecase.GetAccessTokenUseCase
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthUseCaseTest {

    private lateinit var authUseCase: AuthUseCase
    private val getAccessTokenUseCase: GetAccessTokenUseCase = mockk()
    private val tokenRepository: TokenRepository = mockk()

    @Before
    fun setup() {
        authUseCase = AuthUseCase(getAccessTokenUseCase, tokenRepository)
    }

    // 🔹 Testando autenticação bem-sucedida
    @Test
    fun `authenticate should return success when token retrieval and saving are successful`() = runBlocking {
        val fakeTokens = SpotifyTokens("access123", "refresh456")

        coEvery { getAccessTokenUseCase.execute(any(), any()) } returns Result.success(fakeTokens)
        every { tokenRepository.saveTokens(fakeTokens.accessToken, fakeTokens.refreshToken) } returns true

        val result = authUseCase.authenticate("authCode", "redirectUri")

        assertTrue(result.isSuccess)
        assertEquals(fakeTokens, result.getOrNull())

        coVerify(exactly = 1) { getAccessTokenUseCase.execute("authCode", "redirectUri") }
        verify(exactly = 1) { tokenRepository.saveTokens(fakeTokens.accessToken, fakeTokens.refreshToken) }
    }

    // 🔹 Testando falha ao obter o token
    @Test
    fun `authenticate should return failure when token retrieval fails`() = runBlocking {
        coEvery { getAccessTokenUseCase.execute(any(), any()) } returns Result.failure(Exception("Erro ao obter token"))

        val result = authUseCase.authenticate("authCode", "redirectUri")

        assertTrue(result.isFailure)
        assertEquals("Erro ao obter token", result.exceptionOrNull()?.message)

        coVerify(exactly = 1) { getAccessTokenUseCase.execute("authCode", "redirectUri") }
        verify(exactly = 0) { tokenRepository.saveTokens(any(), any()) } // 🔹 Certifica que `saveTokens()` não foi chamado
    }

    // 🔹 Testando falha ao salvar tokens
    @Test
    fun `authenticate should return failure when token saving fails`() = runBlocking {
        val fakeTokens = SpotifyTokens("access123", "refresh456")

        coEvery { getAccessTokenUseCase.execute(any(), any()) } returns Result.success(fakeTokens)
        every { tokenRepository.saveTokens(fakeTokens.accessToken, fakeTokens.refreshToken) } returns false // 🔹 Simulando falha ao salvar

        val result = authUseCase.authenticate("authCode", "redirectUri")

        assertTrue(result.isFailure)
        assertEquals("Erro ao salvar tokens", result.exceptionOrNull()?.message)

        coVerify(exactly = 1) { getAccessTokenUseCase.execute("authCode", "redirectUri") }
        verify(exactly = 1) { tokenRepository.saveTokens(fakeTokens.accessToken, fakeTokens.refreshToken) }
    }
}