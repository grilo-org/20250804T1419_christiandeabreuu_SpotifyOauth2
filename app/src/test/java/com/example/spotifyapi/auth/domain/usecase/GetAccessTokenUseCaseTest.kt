package com.example.spotifyapi.auth.domain.usecase

import com.example.spotifyapi.auth.data.model.SpotifyTokens
import com.example.spotifyapi.auth.data.repository.AuthRepository
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

    @Test
    fun `execute should return SpotifyTokens when API call is successful`() = runBlocking {
        //Given
        val fakeTokens = SpotifyTokens("access123", "refresh456")
        coEvery { repository.getAccessToken(any(), any()) } returns Result.success(fakeTokens)

        //When
        val result = getAccessTokenUseCase.execute("authCode", "redirectUri")

        //Then - sucesso na obtenção do token
        assertTrue(result.isSuccess)
        assertEquals(fakeTokens, result.getOrNull())
        coVerify(exactly = 1) { repository.getAccessToken("authCode", "redirectUri") }
    }

    @Test
    fun `execute should return failure when API call fails`() = runBlocking {
        //Given
        coEvery { repository.getAccessToken(any(), any()) } returns Result.failure(Exception("Erro na API"))

        //When
        val result = getAccessTokenUseCase.execute("authCode", "redirectUri")

        //Then - erro ao chamar getAccessToken()
        assertTrue(result.isFailure)
        assertEquals("Erro na API", result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { repository.getAccessToken("authCode", "redirectUri") }
    }
}