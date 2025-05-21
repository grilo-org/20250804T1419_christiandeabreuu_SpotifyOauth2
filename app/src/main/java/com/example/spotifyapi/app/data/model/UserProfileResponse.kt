package com.example.spotifyapi.app.data.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("display_name") val displayName: String,
    @SerializedName("id") val id: String,
    @SerializedName("images") val images: List<Image>
)

data class Image(
    @SerializedName("url") val url: String
)
