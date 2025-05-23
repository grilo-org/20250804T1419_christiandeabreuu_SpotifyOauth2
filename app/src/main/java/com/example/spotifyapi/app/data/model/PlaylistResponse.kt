package com.example.spotifyapi.app.data.model

import com.google.gson.annotations.SerializedName

data class PlaylistsResponse(
    @SerializedName("items") val items: List<Playlist>
)

data class Playlist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName ("description") val description: String?,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("trackCount") val tracksCount: Int,
    @SerializedName("images") val images: List<Image>?
)

data class Owner(
    @SerializedName("id") val id: String,
    @SerializedName("display_name") val name: String
)


