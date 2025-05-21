package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbumDB
import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.local.ImageArtist
import org.junit.Assert.*
import org.junit.Test

class AlbumMapperTest {

    @Test
    fun `toAlbumDB should correctly map Album to AlbumDB`() {
        val album = Album(
            id = "123",
            name = "Test Album",
            artistId = "artist123",
            releaseDate = "2023-05-20",
            images = listOf(ImageArtist(url = "https://example.com/image.jpg", artistId = 1))
        )

        val albumDB = album.toAlbumDB("artist123")

        assertEquals(album.id, albumDB.databaseId)
        assertEquals(album.name, albumDB.name)
        assertEquals(album.artistId, albumDB.artistId)
        assertEquals(album.releaseDate, albumDB.releaseDate)
        assertEquals(album.images.firstOrNull()?.url, albumDB.imageUrl)
    }

    @Test
    fun `toAlbum should correctly map AlbumDB to Album`() {
        val albumDB = AlbumDB(
            databaseId = "123",
            name = "Test Album",
            artistId = "artist123",
            releaseDate = "2023-05-20",
            imageUrl = "https://example.com/image.jpg"
        )

        val album = albumDB.toAlbum()

        assertEquals(albumDB.databaseId, album.id)
        assertEquals(albumDB.name, album.name)
        assertEquals(albumDB.artistId, album.artistId)
        assertEquals(albumDB.releaseDate, album.releaseDate)
        assertEquals(albumDB.imageUrl, album.images.first().url)
    }

    @Test
    fun `toAlbum should handle null imageUrl`() {
        val albumDB = AlbumDB(
            databaseId = "123",
            name = "Test Album",
            artistId = "artist123",
            releaseDate = "2023-05-20",
            imageUrl = null
        )

        val album = albumDB.toAlbum()

        assertEquals("", album.images.first().url) // Verifica se a URL da imagem fica vazia quando é null
    }
}