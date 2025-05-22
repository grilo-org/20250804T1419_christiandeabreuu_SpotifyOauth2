package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.model.PlaylistsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PlaylistRepositoryTest {

    @RelaxedMockK
    private lateinit var apiService: SpotifyApiService
    @RelaxedMockK
    private lateinit var spotifyDAO: SpotifyDAO
    private lateinit var repository: PlaylistRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = PlaylistRepository(apiService, spotifyDAO)
    }

    @Test
    fun `getPlaylistsFromApi should return playlists when API call is successful`() = runBlocking {
        // Given
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val mockOwner: Owner = mockk(relaxed = true)
        val fakePlaylists =
            listOf(Playlist("id", "name", "description", mockOwner, 0, mockkListImages))
        coEvery { apiService.getPlaylists(any()) } returns PlaylistsResponse(fakePlaylists)

        // When
        val result = repository.getPlaylistsFromApi("token123")

        // Then -Verificando se o resultado corresponde ao esperado
        assertEquals(fakePlaylists, result)
        coVerify(exactly = 1) { apiService.getPlaylists("Bearer token123") }
    }

    @Test
    fun `getPlaylistsFromApi should return empty list when API call fails`() = runBlocking {
        // Given
        coEvery { apiService.getPlaylists(any()) } throws Exception("Erro na API")

        // When
        val result = repository.getPlaylistsFromApi("token123")

        // Then - Verificando se o retorno é uma lista vazia
        assertTrue(result.isEmpty())
    }

    @Test
    fun `insertPlaylistsIntoDB should insert playlists into local database`() = runBlocking {
        // Given
        val fakePlaylistsDB = listOf(
            PlaylistDB(0, "id", "name", "imageUrl", "ownerNAme", 0, "href"),
            PlaylistDB(1, "id1", "name1", "imageUrl1", "ownerNAme1", 1, "href1")
        )

        // When
        repository.insertPlaylistsIntoDB(fakePlaylistsDB)

        // Then - Verificando se o metodo dao foi passado corretamente
        coVerify(exactly = 1) { spotifyDAO.insertLocalPlaylists(fakePlaylistsDB) }
    }

    @Test
    fun `getPlaylistsFromDB should return playlists from local database`() = runBlocking {
        // Given
        val fakePlaylistsDB =
            listOf(PlaylistDB(0, "id", "name", "imageUrl", "ownerNAme", 0, "href"))
        coEvery { spotifyDAO.getLocalPlaylists() } returns fakePlaylistsDB

        // When
        val result = repository.getPlaylistsFromDB()

        // Then - Verificando se os resultados estão corretos
        assertEquals(fakePlaylistsDB, result)
        coVerify(exactly = 1) { spotifyDAO.getLocalPlaylists() }
    }
}