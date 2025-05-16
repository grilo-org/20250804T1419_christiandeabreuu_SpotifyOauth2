package com.example.spotifyapi.app.data.repository

import android.util.Log
import com.example.spotifyapi.app.data.model.Playlist
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class PlaylistRepository(private val apiService: SpotifyApiService) {
    suspend fun getPlaylists(accessToken: String): List<Playlist> {
        return try {
            val response = apiService.getPlaylists("Bearer $accessToken")
            response.items
        } catch (e: Exception) {
            Log.e("PlaylistRepository", "❌ Erro ao buscar playlists: ${e.message}")
            emptyList()
        }
    }
}
