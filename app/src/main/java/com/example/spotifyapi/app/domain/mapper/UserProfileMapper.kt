package com.example.spotifyapi.app.domain.mapper

import com.example.spotifyapi.app.data.local.UserProfileDB
import com.example.spotifyapi.app.data.model.Image
import com.example.spotifyapi.app.data.model.UserProfile

object UserProfileMapper {

    fun UserProfile.toUserProfileDB(): UserProfileDB {
        return UserProfileDB(
            id = this.id,
            name = this.displayName,
            imageUrl = this.images.firstOrNull()?.url.orEmpty()
        )
    }

    fun UserProfileDB.toUserProfile(): UserProfile {
        return UserProfile(
            id = this.id,
            displayName = this.name,
            images = listOf(Image(url = this.imageUrl.orEmpty()))
        )
    }
}