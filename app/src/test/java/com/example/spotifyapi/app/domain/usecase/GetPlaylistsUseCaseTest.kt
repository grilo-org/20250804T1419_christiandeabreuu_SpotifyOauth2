package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.repository.PlaylistRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetPlaylistsUseCaseTest {

    private lateinit var useCase: GetPlaylistsUseCase
    private val repository: PlaylistRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = GetPlaylistsUseCase(repository)
    }

    // 🔹 Teste quando a API retorna playlists corretamente
    @Test
    fun `getPlaylists should return playlists from API when available`() = runBlocking {
        //Given
        val mockOwner: Owner = mockk(relaxed = true)
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val fakePlaylists = listOf(
            Playlist("id", "name", "description", mockOwner, 1, mockkListImages)
        )
        coEvery { repository.getPlaylistsFromApi() } returns fakePlaylists

        //When
        val result = useCase.getPlaylists()

        //Then
        assertEquals(fakePlaylists, result)
        coVerify(exactly = 1) { repository.getPlaylistsFromApi() }
    }

    @Test
    fun `getPlaylists should return empty list when API and DB have no playlists`() = runBlocking {
        // Given
        coEvery { repository.getPlaylistsFromApi() } returns emptyList()
        coEvery { repository.getPlaylistsFromDB() } returns emptyList()

        // When
        val result = useCase.getPlaylists()

        // Then - Teste quando API e banco de dados estão vazios
        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getPlaylistsFromApi() }
        coVerify(exactly = 1) { repository.getPlaylistsFromDB() }
    }
}