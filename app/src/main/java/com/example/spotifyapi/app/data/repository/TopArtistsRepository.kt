package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.local.TopArtistsDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse

interface TopArtistsRepository {
    suspend fun getTopArtistsApi(offset: Int = 0, timeRange: String = "medium_term"): TopArtistsResponse
    suspend fun insertTopArtistsDB(topArtists: TopArtistsDB): Long
    suspend fun insertArtists(artists: List<ArtistDB>): List<Long>
    suspend fun insertImageArtists(imageArtists: List<ImageArtist>)
    suspend fun getArtistDatabaseIdsBySpotifyIds(spotifyIds: List<String>, topArtistsId: Int): List<SpotifyDAO.ArtistIdMap>
}