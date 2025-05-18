package com.example.spotifyapi.app.data.repository

import android.util.Log
import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class UserProfileRepository(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO
) {
    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        return try {
            apiService.getUserProfile("Bearer $accessToken")
        } catch (e: Exception) {
            Log.e("UserProfileRepository", "❌ Erro ao buscar perfil do usuário: ${e.message}")
            null
        }
    }

    suspend fun insertLocalUserProfile(userProfile: UserProfileDB) {
        spotifyDAO.insertLocalUserProfile(userProfile)
        Log.d("UserProfileRepository", "Perfil inserido no banco: $userProfile")
    }

    suspend fun getLocalUserProfile(): UserProfileDB? {
        val userProfile = spotifyDAO.getLocalUserProfile()
        Log.d("UserProfileRepository", "Perfil recuperado do banco: $userProfile")
        return userProfile
    }
}

