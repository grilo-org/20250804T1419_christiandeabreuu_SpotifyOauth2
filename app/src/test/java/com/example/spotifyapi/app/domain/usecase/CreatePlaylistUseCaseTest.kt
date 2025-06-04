package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository
import com.example.spotifyapi.auth.data.plugin.ResourcesPlugin
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
    private val resourcesPlugin = mockk<ResourcesPlugin>(relaxed = true)

    @Before
    fun setup() {
        useCase = CreatePlaylistUseCase(repository, resourcesPlugin)
    }

    @Test
    fun `execute should create playlist when name is valid`() = runBlocking {
        // Given
        coEvery { repository.createPlaylist(any()) } returns true

        // When
        val result = useCase.createPlaylist("token123")

        // Then - Teste de nome de playlist válido
        assertEquals("Playlist 'Minha Playlist' criada com sucesso!", result)
        coVerify(exactly = 1) { repository.createPlaylist("token123") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `execute should throw exception when playlist name is empty`(): Unit = runBlocking {
        // Teste de nome de playlist inválido
        useCase.createPlaylist("token123")
    }

    @Test(expected = Exception::class)
    fun `execute should throw exception when repository fails to create playlist`(): Unit =
        runBlocking {
            coEvery { repository.createPlaylist(any()) } returns false

            // Teste de falha ao criar playlist
            useCase.createPlaylist("token123")
        }
}