package com.example.spotifyapi.app.data.repository

import android.util.Log
import com.example.spotifyapi.app.data.model.Album
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class AlbumsRepository(private val apiService: SpotifyApiService) {
    suspend fun getAlbums(accessToken: String, artistId: String): List<Album> {
        return try {
            val response = apiService.getAlbums("Bearer $accessToken", artistId)
            response.items
        } catch (e: Exception) {
            Log.e("AlbumsRepository", "❌ Erro ao buscar álbuns: ${e.message}")
            emptyList()
        }
    }
}
