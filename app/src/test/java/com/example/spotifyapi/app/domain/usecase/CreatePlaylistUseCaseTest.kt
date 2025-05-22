package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CreatePlaylistUseCaseTest {

    private lateinit var useCase: CreatePlaylistUseCase
    private val repository: CreatePlaylistRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = CreatePlaylistUseCase(repository)
    }

    @Test
    fun `execute should create playlist when name is valid`() = runBlocking {
        // Given
        coEvery { repository.createPlaylist(any(), any()) } returns true

        // When
        val result = useCase.execute("token123", "Minha Playlist")

        // Then - Teste de nome de playlist válido
        assertEquals("Playlist 'Minha Playlist' criada com sucesso!", result)
        coVerify(exactly = 1) { repository.createPlaylist("token123", "Minha Playlist") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `execute should throw exception when playlist name is empty`(): Unit = runBlocking {
        // Teste de nome de playlist inválido
        useCase.execute("token123", "")
    }

    @Test(expected = Exception::class)
    fun `execute should throw exception when repository fails to create playlist`(): Unit =
        runBlocking {
            coEvery { repository.createPlaylist(any(), any()) } returns false

            // Teste de falha ao criar playlist
            useCase.execute("token123", "Erro Playlist")
        }
}