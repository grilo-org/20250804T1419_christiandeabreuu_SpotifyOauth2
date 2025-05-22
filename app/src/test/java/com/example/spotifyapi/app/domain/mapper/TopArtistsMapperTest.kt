package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper.toTopArtistsDB
import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper.toArtistsDB
import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper.toImageArtistsDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.ImageArtistResponse
import org.junit.Assert.*
import org.junit.Test

class TopArtistsMapperTest {

    @Test
    fun `toTopArtistsDB should correctly map TopArtistsResponse to TopArtistsDB`() {
        // Given
        val response = TopArtistsResponse(
            items = listOf(),
            total = 100,
            limit = 20,
            offset = 0,
            href = "https://api.spotify.com/v1/me/top/artists",
            next = null,
            previous = null
        )
        // When
        val result = toTopArtistsDB(response, "medium_term")

        // Then
        assertEquals(response.total, result.total)
        assertEquals(response.limit, result.limit)
        assertEquals(response.offset, result.offset)
        assertEquals(response.href, result.href)
        assertEquals(response.next, result.next)
        assertEquals(response.previous, result.previous)
        assertEquals("medium_term", result.timeRange)
    }

    @Test
    fun `toArtistsDB should correctly map TopArtistsResponse to List ArtistDB`() {
        // Given
        val response = TopArtistsResponse(
            items = listOf(
                ArtistResponse("1", "Artist 1", 90, listOf(ImageArtistResponse("url1"))),
                ArtistResponse("2", "Artist 2", 80, listOf(ImageArtistResponse("url2")))
            ),
            total = 2,
            limit = 20,
            offset = 0,
            href = "https://api.spotify.com/v1/me/top/artists",
            next = null,
            previous = null
        )
        // When
        val result = toArtistsDB(response, 101)

        // Then - Verificando se os atributos estão corretos
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("Artist 1", result[0].name)
        assertEquals(90, result[0].popularity)
        assertEquals(101, result[0].topArtistsId)
        assertEquals("2", result[1].id)
        assertEquals("Artist 2", result[1].name)
        assertEquals(80, result[1].popularity)
        assertEquals(101, result[1].topArtistsId)
    }

    @Test
    fun `toImageArtistsDB should correctly map TopArtistsResponse to List ImageArtist`() {
        // Given
        val response = TopArtistsResponse(
            items = listOf(
                ArtistResponse("1", "Artist 1", 90, listOf(ImageArtistResponse("url1"), ImageArtistResponse("url3"))),
                ArtistResponse("2", "Artist 2", 80, listOf(ImageArtistResponse("url2")))
            ),
            total = 2,
            limit = 20,
            offset = 0,
            href = "https://api.spotify.com/v1/me/top/artists",
            next = null,
            previous = null
        )
        val artistsIds = listOf(10L, 20L)

        // When
        val result = toImageArtistsDB(response, artistsIds)

        // Then - Verificando se os atributos estão corretos
        assertEquals(3, result.size)
        assertEquals("url1", result[0].url)
        assertEquals(10, result[0].artistId)
        assertEquals("url3", result[1].url)
        assertEquals(10, result[1].artistId)
        assertEquals("url2", result[2].url)
        assertEquals(20, result[2].artistId)
    }
}