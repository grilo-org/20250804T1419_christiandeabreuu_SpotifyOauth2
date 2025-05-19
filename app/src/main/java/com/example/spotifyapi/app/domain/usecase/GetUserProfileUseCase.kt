package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile
import com.example.spotifyapi.app.data.repository.UserProfileRepository

class GetUserProfileUseCase(private val repository: UserProfileRepository) {

    suspend fun execute(accessToken: String): UserProfile? {
        return fetchUserProfile(accessToken) ?: getLocalUserProfile()
    }

    private suspend fun fetchUserProfile(accessToken: String): UserProfile? {
        val responseApi = repository.getUserProfileFromApi(accessToken) ?: return null

        saveUserProfile(responseApi)
        return responseApi
    }

    private suspend fun saveUserProfile(response: UserProfile) {
        val userProfileDB = UserProfileDB(
            id = response.id,
            name = response.displayName,
            imageUrl = response.images.firstOrNull()?.url
        )
        repository.insertLocalUserProfile(userProfileDB)
    }

    private suspend fun getLocalUserProfile(): UserProfile? {
        return repository.getLocalUserProfile()?.let { mapToUserProfile(it) }
    }

    private fun mapToUserProfile(userProfileDB: UserProfileDB): UserProfile {
        return UserProfile(
            id = userProfileDB.id,
            displayName = userProfileDB.name,
            images = listOf(Image(url = userProfileDB.imageUrl ?: ""))
        )
    }
}