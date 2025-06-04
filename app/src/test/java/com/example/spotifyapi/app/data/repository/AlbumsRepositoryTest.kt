package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.model.AlbumsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class AlbumsRepositoryTest {

    private var apiService: SpotifyApiService = mockk(relaxed = true)
    private var spotifyDAO: SpotifyDAO = mockk(relaxed = true)
    private var repository: AlbumsRepository = mockk(relaxed = true)
    private var tokenRepository: TokenRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = AlbumsRepositoryImpl(apiService, spotifyDAO, tokenRepository)
    }

    @Test
    fun `getAlbumsFromDB should return albums from local database`() = runBlocking {
        // Given
        val fakeAlbums = listOf(
            AlbumDB("databaseId", "name", "artistId", "imageUrl", "releaseDate"),
            AlbumDB("databaseId2", "name2", "artistId2", "imageUrl2", "releaseDate2")
        )
        coEvery { spotifyDAO.getLocalAlbumsByArtist("123") } returns fakeAlbums

        // When
        val result = repository.getAlbumsFromDB("123")

        // Then - Verificando se os resultados estão corretos -
        assertEquals(fakeAlbums, result)
        coVerify(exactly = 1) { spotifyDAO.getLocalAlbumsByArtist("123") }
    }

    @Test
    fun `getAlbumsFromApi should return albums from API`() = runBlocking {
        // Given
        val fakeResponse = AlbumsResponse(emptyList())
        coEvery { apiService.getAlbums(any(), "123") } returns fakeResponse

        // When
        val result = repository.getAlbumsFromApi("token")

        // Then - Verificando se os resultados estão corretos
        assertNotNull(result)
        assertEquals(fakeResponse, result)
    }

    @Test
    fun `insertLocalAlbums should insert albums into local database`() = runBlocking {
        // Given
        val fakeAlbums =
            listOf(AlbumDB("databaseId", "name", "artistId", "imageUrl", "releaseDate"))

        // When
        repository.insertLocalAlbums(fakeAlbums)

        // Then - Verificando se o metodo dao foi passado corretamente
        coVerify(exactly = 1) { spotifyDAO.insertLocalAlbums(fakeAlbums) }
    }
}