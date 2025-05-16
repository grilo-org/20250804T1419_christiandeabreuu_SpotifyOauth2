package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.Artist
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.repository.UserProfileRepository

class GetUserProfileTopArtistsUseCase(private val repository: UserProfileRepository) {
    suspend fun execute(accessToken: String): UserProfile? {
        return repository.getUserProfile(accessToken)
    }
}