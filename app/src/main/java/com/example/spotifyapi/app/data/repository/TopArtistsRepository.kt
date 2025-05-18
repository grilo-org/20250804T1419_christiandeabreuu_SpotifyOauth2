package com.example.spotifyapi.app.data.repository

import android.util.Log
import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class TopArtistsRepository(private val apiService: SpotifyApiService) {
    suspend fun getTopArtists(accessToken: String): List<Artist> {
        return try {
            val response = apiService.getTopArtists("Bearer $accessToken")
            response.items
        } catch (e: Exception) {
            Log.e("TopArtistsRepository", "❌ Erro ao buscar artistassss: ${e.message}")
            emptyList()
        }
    }
}