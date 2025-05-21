package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.repository.AlbumsRepository
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAlbumsUseCaseTest {

    private lateinit var useCase: GetAlbumsUseCase
    private val repository: AlbumsRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = GetAlbumsUseCase(repository)
    }

    // 🔹 Teste quando há álbuns no banco de dados
    @Test
    fun `execute should return albums from database when available`() = runBlocking {
        val fakeAlbumsDb = listOf(
            AlbumDB(
                "databaseId",
                "name",
                "releaseDate",
                "imageUrl",
                "artistId"
            ) // 🔹 Certifique-se da ordem correta dos parâmetros
        )

        val mockkListAlbum: List<ImageArtist> = listOf(
            ImageArtist(1, "a,", 1), ImageArtist(1, "a,", 1), ImageArtist(1, "a,", 1)
        )

        val fakeAlbums =
            fakeAlbumsDb.map { it.toAlbum() } // 🔹 Converte `AlbumDB` para `Album` antes da comparação

        coEvery { repository.getAlbumsFromDB(any()) } returns fakeAlbumsDb

        val result = useCase.execute("token123", "artist123")

        assertEquals(fakeAlbums, result) // 🔹 Agora a comparação será entre objetos do mesmo tipo
        coVerify(exactly = 1) { repository.getAlbumsFromDB("artist123") }
    }

    // 🔹 Teste quando API e banco de dados estão vazios
    @Test
    fun `execute should return empty list when API and database have no albums`() = runBlocking {
        coEvery { repository.getAlbumsFromDB(any()) } returns emptyList()
        coEvery { repository.getAlbumsFromApi(any(), any()) } returns null

        val result = useCase.execute("token123", "artist123")

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getAlbumsFromDB("artist123") }
        coVerify(exactly = 1) { repository.getAlbumsFromApi("token123", "artist123") }
    }
}