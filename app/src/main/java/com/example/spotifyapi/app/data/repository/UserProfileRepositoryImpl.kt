package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.database.SpotifyDAO
import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.auth.data.repository.TokenRepository
import com.example.spotifyapi.utils.addBearer

class UserProfileRepositoryImpl(
    private val apiService: SpotifyApiService,
    private val spotifyDAO: SpotifyDAO,
    private val tokenRepository: TokenRepository
) : UserProfileRepository {
    override suspend fun getUserProfileFromApi(): UserProfile? {
        return try {
            val token = tokenRepository.getAccessToken().orEmpty()
            apiService.getUserProfile(token.addBearer())
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertLocalUserProfile(userProfile: UserProfileDB) {
        spotifyDAO.insertLocalUserProfile(userProfile)
    }

    override suspend fun getLocalUserProfile(): UserProfileDB? {
        return spotifyDAO.getLocalUserProfile()
    }
}