package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.Owner
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylist
import com.example.spotifyapi.app.domain.mapper.PlaylistMapper.toPlaylistDB
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaylistMapperTest {

    @Test
    fun `toPlaylistDB should correctly map Playlist to PlaylistDB`() {
        //Given
        val playlist = Playlist(
            id = "123",
            name = "Rock Hits",
            description = "Best rock songs",
            owner = Owner(id = "owner1", name = "John Doe"),
            tracksCount = 25,
            images = listOf(Image(url = "https://example.com/image.jpg"))
        )

        //When
        val playlistDB = playlist.toPlaylistDB()

        //Then - Verificando se os atributos estão corretos
        assertEquals(playlist.id, playlistDB.id)
        assertEquals(playlist.name, playlistDB.name)
        assertEquals(playlist.description, playlistDB.description)
        assertEquals(playlist.owner.name, playlistDB.ownerName)
        assertEquals(playlist.tracksCount, playlistDB.tracksCount)
        assertEquals(playlist.images?.firstOrNull()?.url, playlistDB.imageUrl)
    }

    @Test
    fun `toPlaylist should correctly map PlaylistDB to Playlist`() {
        //Given
        val playlistDB = PlaylistDB(
            id = "123",
            name = "Rock Hits",
            description = "Best rock songs",
            ownerName = "John Doe",
            tracksCount = 25,
            imageUrl = "https://example.com/image.jpg"
        )

        //When
        val playlist = playlistDB.toPlaylist()

        //Then - Verificando se os atributos estão corretos
        assertEquals(playlistDB.id, playlist.id)
        assertEquals(playlistDB.name, playlist.name)
        assertEquals(playlistDB.description, playlist.description)
        assertEquals(playlistDB.ownerName, playlist.owner.name)
        assertEquals(playlistDB.tracksCount, playlist.tracksCount)
        assertEquals(playlistDB.imageUrl, playlist.images?.first()?.url ?: "")
    }

    @Test
    fun `toPlaylist should handle blank imageUrl`() {
        // Given
        val playlistDB = PlaylistDB(
            id = "123",
            name = "Rock Hits",
            description = "Best rock songs",
            ownerName = "John Doe",
            tracksCount = 25,
            imageUrl = ""
        )

        // When
        val playlist = playlistDB.toPlaylist()

        // Then - Verificando se a URL da imagem fica vazia quando é blank
        playlist.images?.let { assertTrue(it.isEmpty()) }
    }
}