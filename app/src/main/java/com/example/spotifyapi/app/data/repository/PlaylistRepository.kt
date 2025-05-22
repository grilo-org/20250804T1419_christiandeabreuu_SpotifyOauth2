package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository

class PlaylistRepository(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository

) {
    suspend fun getPlaylistsFromApi(): List<Playlist> {
        return try {
            val token = tokenRepository.getAccessToken()
            val response = apiService.getPlaylists("Bearer $token")
            response.items
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun insertPlaylistsIntoDB(playlists: List<PlaylistDB>) {
        spotifyDAO.insertLocalPlaylists(playlists)
    }

    suspend fun getPlaylistsFromDB(): List<PlaylistDB> {
        val playlists = spotifyDAO.getLocalPlaylists()
        return playlists
    }
}
