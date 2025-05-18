package com.example.spotifyapi.app.data.repository

import android.util.Log
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
            Log.e("PlaylistRepository", "❌ Erro ao buscar playlists: ${e.message}")
            emptyList()
        }
    }

    suspend fun insertPlaylistsIntoDB(playlists: List<PlaylistDB>) {
        spotifyDAO.insertPlaylists(playlists)
    }

    suspend fun getPlaylistsFromDB(): List<PlaylistDB> {
        val playlists = spotifyDAO.getPlaylists()
        Log.d("PlaylistRepository", "📀 Playlists no banco antes da conversão: ${playlists.size}")
        Log.d("PlaylistRepository", "🖼️ URLs das imagens no banco: ${playlists.map { it.imageUrl }}")
        return playlists
    }
}
