package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.AlbumsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
//
//    @Test
//    fun `getAlbumsFromApi should return albums from API`() = runBlocking {
//        // Given
//        val fakeResponse = AlbumsResponse(emptyList())
//        coEvery { apiService.getAlbums(any(), "123") } returns fakeResponse
//
//        // When
//        val result = repository.getAlbumsFromApi("token")
//
//        // Then - Verificando se os resultados estão corretos
//        assertNotNull(result)
//        assertEquals(fakeResponse, result)
//    }

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


    @Test
    fun `insertLocalAlbums should call DAO correctly`() = runTest {
        val fakeAlbums = listOf(AlbumDB("1", "Album A", "2023", "", ""))
        coEvery { spotifyDAO.insertLocalAlbums(fakeAlbums) } just Runs

        repository.insertLocalAlbums(fakeAlbums)

        coVerify { spotifyDAO.insertLocalAlbums(fakeAlbums) }
    }

    @Test
    fun `getAlbumsFromApi should return answer correctly`() = runTest {
        val fakeResponse = AlbumsResponse(items = emptyList())
        coEvery { tokenRepository.getAccessToken() } returns "fake_token"
        coEvery { apiService.getAlbums("Bearer fake_token", "artist123") } returns fakeResponse

        val result = repository.getAlbumsFromApi("artist123")

        assertEquals(fakeResponse, result)
    }

    @Test
    fun `getAlbumsFromApi should call null in error`() = runTest {
        coEvery { tokenRepository.getAccessToken() } returns "fake_token"
        coEvery { apiService.getAlbums(any(), any()) } throws Exception("Erro na API")

        val result = repository.getAlbumsFromApi("artist123")

        assertEquals(null, result)
    }

    @Test
    fun `getAlbumsFromDB should retunr album list`() = runTest {
        val fakeAlbums = listOf(AlbumDB("1", "Album A", "2023", "", ""))
        coEvery { spotifyDAO.getLocalAlbumsByArtist("artist123") } returns fakeAlbums

        val result = repository.getAlbumsFromDB("artist123")

        assertEquals(fakeAlbums, result)
    }
}
