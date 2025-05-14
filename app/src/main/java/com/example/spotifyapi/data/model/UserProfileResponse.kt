package com.example.spotifyapi.data.model

import com.google.gson.annotations.SerializedName

class UserProfileResponse {

    data class UserProfile(
        @SerializedName("display_name")
        val displayName: String,
        val id: String,
        val images: List<Image>
    )

    data class Image(
        val url: String
    )
}