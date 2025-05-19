package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService

class UserProfileRepository(
    private val apiService: SpotifyApiService, private val spotifyDAO: SpotifyDAO
) {
    suspend fun getUserProfileFromApi(accessToken: String): UserProfile? {
        return try {
            apiService.getUserProfile("Bearer $accessToken")
        } catch (e: Exception) {
            null
        }
    }

    suspend fun insertLocalUserProfile(userProfile: UserProfileDB) {
        spotifyDAO.insertLocalUserProfile(userProfile)
    }

    suspend fun getLocalUserProfile(): UserProfileDB? {
        val userProfile = spotifyDAO.getLocalUserProfile()
        return userProfile
    }
}

