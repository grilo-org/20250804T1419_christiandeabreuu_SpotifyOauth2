package com.example.spotifyapi.app.data.repository

import android.util.Log
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtistDB
import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.model.TopArtistInfoResponse
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class TopArtistsRepository(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService
) {
    suspend fun getTopArtists(accessToken: String): List<TopArtistInfoResponse> {
        return try {
            val response = apiService.getTopArtists("Bearer $accessToken")
            Log.d("API Response", "🎵 Artistas recebidos: ${response.items.size}")
            response.items
        } catch (e: Exception) {
            Log.e("TopArtistsRepository", "❌ Erro ao buscar artistas: ${e.message}")
            emptyList() // Evita falhas na inserção no banco
        }
    }

    suspend fun insertLocalTopArtists(artists: List<ArtistDB>) {
        spotifyDAO.insertLocalTopArtistsInfo(artists)
    }

    suspend fun getLocalTopArtists(): List<ArtistDB> {
        return spotifyDAO.getLocalTopArtistsInfo()
    }

    suspend fun insertLocalImages(images: List<ImageArtistDB>) {
        spotifyDAO.insertLocalTopArtistsImages(images)
    }

    suspend fun getLocalArtistImages(): List<ImageArtistDB> {
        return spotifyDAO.getLocalTopArtistImages()
    }
}