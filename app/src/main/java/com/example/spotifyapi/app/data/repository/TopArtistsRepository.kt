package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.local.TopArtistsDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.Constants.BEARER
import com.example.spotifyapi.utils.addBearer

class TopArtistsRepository(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) {
    suspend fun getTopArtistsApi(
        offset: Int = 0, timeRange: String = "medium_term"
    ): TopArtistsResponse {
        val token = tokenRepository.getAccessToken().orEmpty()
        return apiService.getTopArtists(
            accessToken = token.addBearer(), limit = 20, timeRange = timeRange, offset = offset
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