package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.repository.UserProfileRepository
import com.example.spotifyapi.app.domain.mapper.UserProfileMapper.toUserProfile
import com.example.spotifyapi.app.domain.mapper.UserProfileMapper.toUserProfileDB
import com.example.spotifyapi.app.data.model.UserProfile

class GetUserProfileUseCase(private val repository: UserProfileRepository) {

    suspend fun execute(accessToken: String): UserProfile? {
        return fetchUserProfile(accessToken) ?: getLocalUserProfile()
    }

    private suspend fun fetchUserProfile(accessToken: String): UserProfile? {
        val responseApi = repository.getUserProfileFromApi(accessToken) ?: return null
        repository.insertLocalUserProfile(responseApi.toUserProfileDB())
        return responseApi
    }

    private suspend fun getLocalUserProfile(): UserProfile? {
        return repository.getLocalUserProfile()?.toUserProfile()
    }
}