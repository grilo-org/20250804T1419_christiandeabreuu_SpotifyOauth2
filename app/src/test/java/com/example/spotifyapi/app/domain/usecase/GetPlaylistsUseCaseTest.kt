package com.example.spotifyapi.app.domain.usecase

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.spotifyapi.app.data.repository.PlaylistRepository
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylist
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylistDB

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
        val mockkPlaylist1: Playlist = mockk(relaxed = true)
        val mockkPlaylist2: Playlist = mockk(relaxed = true)
        val mockOwner: Owner = mockk(relaxed = true)

        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))


        val fakePlaylists = listOf(
            Playlist("id", "name", "description", mockOwner, 1, mockkListImages)
        )

        coEvery { repository.getPlaylistsFromApi(any()) } returns fakePlaylists

        val result = useCase.getPlaylists("token123")

        assertEquals(fakePlaylists, result)
        coVerify(exactly = 1) { repository.getPlaylistsFromApi("token123") }
    }

    // 🔹 Teste quando a API retorna vazia e busca no banco de dados
    @Test
    fun `getPlaylists should return playlists from DB when API returns empty`() = runBlocking {
        val fakePlaylistsDB = listOf(
            PlaylistDB(0, "id", "name", "description", "ownername", 1, mockk(relaxed = true)),
            PlaylistDB(1, "id1", "name1", "description1", "ownername1", 11, mockk(relaxed = true)),

            )

        coEvery { repository.getPlaylistsFromApi(any()) } returns emptyList()
        coEvery { repository.getPlaylistsFromDB() } returns fakePlaylistsDB

        val result = useCase.getPlaylists("token123")

        assertEquals(fakePlaylistsDB.map { it.toPlaylist() }, result)
        coVerify(exactly = 1) { repository.getPlaylistsFromApi("token123") }
        coVerify(exactly = 1) { repository.getPlaylistsFromDB() }
    }

    // 🔹 Teste para salvar playlists no banco sem duplicatas
    @Test
    fun `savePlaylistsToDB should filter duplicates before inserting`() = runBlocking {
        val mockkListImages: List<Image> =
            listOf(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true))
        val mockOwner: Owner = mockk(relaxed = true)
        val existingPlaylists = listOf(
            PlaylistDB(0, "id", "name", "description", "ownername", 1, mockk(relaxed = true)),
        )
        val newPlaylists =
            listOf(Playlist("id", "name", "description", mockOwner, 0, mockkListImages))

        coEvery { repository.getPlaylistsFromDB() } returns existingPlaylists
        coEvery { repository.insertPlaylistsIntoDB(any()) } just Runs

        useCase.savePlaylistsToDB(newPlaylists)

        coVerify(exactly = 1) {
            repository.insertPlaylistsIntoDB(
                listOf(
                    newPlaylists.first().toPlaylistDB()
                )
            )
        }
    }

    // 🔹 Teste quando API e banco de dados estão vazios
    @Test
    fun `getPlaylists should return empty list when API and DB have no playlists`() = runBlocking {
        coEvery { repository.getPlaylistsFromApi(any()) } returns emptyList()
        coEvery { repository.getPlaylistsFromDB() } returns emptyList()

        val result = useCase.getPlaylists("token123")

        assertTrue(result.isEmpty())
        coVerify(exactly = 1) { repository.getPlaylistsFromApi("token123") }
        coVerify(exactly = 1) { repository.getPlaylistsFromDB() }
    }
}