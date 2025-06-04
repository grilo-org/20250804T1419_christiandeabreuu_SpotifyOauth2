package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.local.TopArtistsDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class TopArtistsRepositoryImpl(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) : TopArtistsRepository {
    override suspend fun getTopArtistsApi(
        offset: Int, timeRange: String
    ): TopArtistsResponse {
        val token = tokenRepository.getAccessToken().orEmpty()
        return apiService.getTopArtists(
            accessToken = token.addBearer(), limit = 50, timeRange = timeRange, offset = offset
        )
    }

    override suspend fun insertTopArtistsDB(topArtists: TopArtistsDB): Long {
        return spotifyDAO.insertTopArtistsDB(topArtists)
    }

    override suspend fun insertArtists(artists: List<ArtistDB>): List<Long> {
        return spotifyDAO.insertArtists(artists)
    }

    override suspend fun insertImageArtists(imageArtists: List<ImageArtist>) {
        spotifyDAO.insertImageArtists(imageArtists)
    }

    override suspend fun getArtistDatabaseIdsBySpotifyIds(spotifyIds: List<String>, topArtistsId: Int) =
        spotifyDAO.getArtistDatabaseIdsBySpotifyIds(spotifyIds, topArtistsId)
}