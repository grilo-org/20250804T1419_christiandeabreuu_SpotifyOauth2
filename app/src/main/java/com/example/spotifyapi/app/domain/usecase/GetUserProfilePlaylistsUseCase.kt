package com.example.spotifyapi.app.domain.usecase


import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.data.model.UserProfile

class GetUserProfilePlaylistsUseCase(private val repository: UserProfileRepository) {
    suspend fun execute(accessToken: String): UserProfile? {
        return repository.getUserProfileFromApi(accessToken)
    }
}