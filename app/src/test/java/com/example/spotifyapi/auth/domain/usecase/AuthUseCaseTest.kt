package com.example.spotifyapi.auth.domain.usecase

import com.example.spotifyapi.auth.data.model.SpotifyTokens
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
import com.example.spotifyapi.auth.data.repository.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthUseCaseTest {

    private lateinit var authUseCase: AuthUseCase
    private val getAccessTokenUseCase: GetAccessTokenUseCase = mockk()
    private val tokenRepository: TokenRepository = mockk()
    private val resourcesPlugin: ResourcesPlugin = mockk()

    @Before
    fun setup() {
        authUseCase = AuthUseCase(getAccessTokenUseCase, tokenRepository, resourcesPlugin)
    }

    @Test
    fun `authenticate should return success when token retrieval and saving are successful`() =
        runBlocking {
            // Given
            val fakeTokens = SpotifyTokens("access123", "refresh456")
            coEvery { getAccessTokenUseCase.execute(any(), any()) } returns Result.success(
                fakeTokens
            )
            every {
                tokenRepository.saveTokens(
                    fakeTokens.accessToken, fakeTokens.refreshToken
                )
            } returns true

            // When
            val result = authUseCase.authenticate("authCode", "redirectUri")

            // Then - autenticação bem-sucedida
            assertTrue(result.isSuccess)
            assertEquals(fakeTokens, result.getOrNull())
            coVerify(exactly = 1) { getAccessTokenUseCase.execute("authCode", "redirectUri") }
            verify(exactly = 1) {
                tokenRepository.saveTokens(
                    fakeTokens.accessToken, fakeTokens.refreshToken
                )
            }
        }

    @Test
    fun `authenticate should return failure when token retrieval fails`() = runBlocking {
        //Given
        coEvery {
            getAccessTokenUseCase.execute(
                any(), any()
            )
        } returns Result.failure(Exception("Erro ao obter token"))

        //When
        val result = authUseCase.authenticate("authCode", "redirectUri")

        //Then -  falha ao obter o token e que savetokens nao foi chamado
        assertTrue(result.isFailure)
        assertEquals("Erro ao obter token", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { getAccessTokenUseCase.execute("authCode", "redirectUri") }
        verify(exactly = 0) {
            tokenRepository.saveTokens(
                any(), any()
            )
        }
    }


    @Test
    fun `authenticate should return failure when token saving fails`() = runBlocking {
        //Given
        val fakeTokens = SpotifyTokens("access123", "refresh456")
        coEvery { getAccessTokenUseCase.execute(any(), any()) } returns Result.success(fakeTokens)
        every {
            tokenRepository.saveTokens(
                fakeTokens.accessToken, fakeTokens.refreshToken
            )
        } returns false

        //When
        val result = authUseCase.authenticate("authCode", "redirectUri")

        //Then - Testando falha ao salvar tokens
        assertTrue(result.isFailure)
        assertEquals("Erro ao salvar tokens", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { getAccessTokenUseCase.execute("authCode", "redirectUri") }
        verify(exactly = 1) {
            tokenRepository.saveTokens(
                fakeTokens.accessToken, fakeTokens.refreshToken
            )
        }
    }
}