package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.ImageArtistResponse
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class TopArtistsMapperTest {

    @Test
    fun `toArtistsDB maps TopArtistsResponse to ArtistDB list correctly`() {
        // Arrange
        val imageArtistResponde = ImageArtistResponse(url = "http://img.com/1.jpg")
        val artistImage = Image(url = "http://img.com/1.jpg")
        val artist = ArtistResponse(
            id = "artist1",
            name = "Artist One",
            popularity = 88,
            images = listOf(imageArtistResponde),
            // adicione outros campos obrigatórios do seu modelo Artist
        )
        val topArtistsResponse = TopArtistsResponse(
            items = listOf(artist),
            total = 1,
            limit = 20,
            offset = 0,
            href = "http://api.com/artists",
            next = "http://api.com/artists?page=2",
            previous = null
        )
        val timeRange = "short_term"

        // Act
        val result = TopArtistsMapper.toArtistsDB(topArtistsResponse, timeRange)

        // Assert
        val expected = listOf(
            ArtistDB(
                id = "artist1",
                name = "Artist One",
                popularity = 88,
                imageUrl = "http://img.com/1.jpg",
                timeRange = "short_term",
                total = 1,
                limit = 20,
                offset = 0,
                href = "http://api.com/artists",
                next = "http://api.com/artists?page=2",
                previous = null
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toArtistsDB maps empty images to empty imageUrl`() {
        // Arrange
        val artist = ArtistResponse(
            id = "artist2",
            name = "Artist Two",
            popularity = 50,
            images = emptyList(),
            // adicione outros campos obrigatórios do seu modelo Artist
        )
        val topArtistsResponse = TopArtistsResponse(
            items = listOf(artist),
            total = 1,
            limit = 20,
            offset = 0,
            href = null,
            next = null,
            previous = null
        )
        val timeRange = "long_term"

        // Act
        val result = TopArtistsMapper.toArtistsDB(topArtistsResponse, timeRange)

        // Assert
        assertEquals("", result[0].imageUrl)
    }
}