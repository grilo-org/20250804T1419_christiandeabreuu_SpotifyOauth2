package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class PlaylistRepository(private val apiService: SpotifyApiService
    , private val spotifyDAO: SpotifyDAO
) {
    suspend fun getPlaylistsFromApi(accessToken: String): List<Playlist> {
        return try {
            val response = apiService.getPlaylists("Bearer $accessToken")
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
