package com.example.spotifyapi.app.data.repository

import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfileFromApi(): UserProfile?
    suspend fun insertLocalUserProfile(userProfile: UserProfileDB)
    suspend fun getLocalUserProfile(): UserProfileDB?
}