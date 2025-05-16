package com.example.spotifyapi.app.data.repository

import android.util.Log
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class UserProfileRepository(private val apiService: SpotifyApiService) {
    suspend fun getUserProfile(accessToken: String): UserProfile? {
        return try {
            apiService.getUserProfile("Bearer $accessToken")
        } catch (e: Exception) {
            Log.e("UserProfileRepository", "❌ Erro ao buscar perfil do usuário: ${e.message}")
            null
        }
    }
}

