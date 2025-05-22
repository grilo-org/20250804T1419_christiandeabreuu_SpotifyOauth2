package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.TopArtistsDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class TopArtistsRepository(
    private val apiService: SpotifyApiService, private val spotifyDAO: SpotifyDAO
) {
    suspend fun getTopArtistsApi(
        accessToken: String, offset: Int = 0, timeRange: String = "medium_term"
    ): TopArtistsResponse {
        return apiService.getTopArtists(
            accessToken = "Bearer $accessToken", limit = 20, timeRange = timeRange, offset = offset
        )
    }

    suspend fun insertTopArtistsDB(topArtists: TopArtistsDB): Long {
        return spotifyDAO.insertTopArtistsDB(topArtists)
    }

    suspend fun insertArtists(artists: List<ArtistDB>): List<Long> {
        return spotifyDAO.insertArtists(artists)
    }

    suspend fun insertImageArtists(imageArtists: List<ImageArtist>) {
        spotifyDAO.insertImageArtists(imageArtists)
    }

}