package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbum
import com.example.spotifyapi.app.domain.mapper.AlbumMapper.toAlbumDB
import org.junit.Assert.assertEquals
import org.junit.Test

class AlbumMapperTest {

    @Test
    fun `toAlbumDB should correctly map Album to AlbumDB`() {
        //Given
        val album = Album(
            id = "123",
            name = "Test Album",
            artistId = "artist123",
            releaseDate = "2023-05-20",
            images = listOf(ImageArtist(url = "https://example.com/image.jpg", artistId = 1))
        )

        //When
        val albumDB = album.toAlbumDB("artist123")

        //Then - Verificando se os atributos estão corretos
        assertEquals(album.id, albumDB.databaseId)
        assertEquals(album.name, albumDB.name)
        assertEquals(album.artistId, albumDB.artistId)
        assertEquals(album.releaseDate, albumDB.releaseDate)
        assertEquals(album.images.firstOrNull()?.url, albumDB.imageUrl)
    }

    @Test
    fun `toAlbum should correctly map AlbumDB to Album`() {
        //Given
        val albumDB = AlbumDB(
            databaseId = "123",
            name = "Test Album",
            artistId = "artist123",
            releaseDate = "2023-05-20",
            imageUrl = "https://example.com/image.jpg"
        )
        //When
        val album = albumDB.toAlbum()

        //Then - Verificando se os atributos estão corretos
        assertEquals(albumDB.databaseId, album.id)
        assertEquals(albumDB.name, album.name)
        assertEquals(albumDB.artistId, album.artistId)
        assertEquals(albumDB.releaseDate, album.releaseDate)
        assertEquals(albumDB.imageUrl, album.images.first().url)
    }

    @Test
    fun `toAlbum should handle null imageUrl`() {
        //Given
        val albumDB = AlbumDB(
            databaseId = "123",
            name = "Test Album",
            artistId = "artist123",
            releaseDate = "2023-05-20",
            imageUrl = null
        )

        //When
        val album = albumDB.toAlbum()

        //Then  Verifica se a URL da imagem fica vazia quando é null
        assertEquals("", album.images.first().url)
    }
}