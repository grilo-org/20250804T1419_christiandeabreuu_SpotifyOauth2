package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.domain.mapper.UserProfileMapper.toUserProfile
import com.example.spotifyapi.app.domain.mapper.UserProfileMapper.toUserProfileDB
import com.example.spotifyapi.app.data.model.UserProfile

class GetUserProfileUseCase(private val repository: UserProfileRepository) {

    suspend fun execute(): UserProfile? {
        return fetchUserProfile() ?: getLocalUserProfile()
    }

    private suspend fun fetchUserProfile(): UserProfile? {
        val responseApi = repository.getUserProfileFromApi() ?: return null
        repository.insertLocalUserProfile(responseApi.toUserProfileDB())
        return responseApi
    }

    private suspend fun getLocalUserProfile(): UserProfile? {
        return repository.getLocalUserProfile()?.toUserProfile()
    }
}