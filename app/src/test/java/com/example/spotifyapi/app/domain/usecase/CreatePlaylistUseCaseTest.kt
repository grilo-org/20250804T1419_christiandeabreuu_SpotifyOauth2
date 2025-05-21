package com.example.spotifyapi.app.domain.usecase

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.spotifyapi.app.domain.usecase.CreatePlaylistUseCase
import com.example.spotifyapi.app.data.repository.CreatePlaylistRepository

class CreatePlaylistUseCaseTest {

    private lateinit var useCase: CreatePlaylistUseCase
    private val repository: CreatePlaylistRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = CreatePlaylistUseCase(repository)
    }

    // 🔹 Teste de nome de playlist válido
    @Test
    fun `execute should create playlist when name is valid`() = runBlocking {
        coEvery { repository.createPlaylist(any(), any()) } returns true

        val result = useCase.execute("token123", "Minha Playlist")

        assertEquals("Playlist 'Minha Playlist' criada com sucesso!", result)
        coVerify(exactly = 1) { repository.createPlaylist("token123", "Minha Playlist") }
    }

    // 🔹 Teste de nome de playlist inválido
    @Test(expected = IllegalArgumentException::class)
    fun `execute should throw exception when playlist name is empty`(): Unit = runBlocking {
        useCase.execute("token123", "")
    }

    // 🔹 Teste de erro ao criar playlist
    @Test(expected = Exception::class)
    fun `execute should throw exception when repository fails to create playlist`(): Unit = runBlocking {
        coEvery { repository.createPlaylist(any(), any()) } returns false

        useCase.execute("token123", "Erro Playlist")
    }
}