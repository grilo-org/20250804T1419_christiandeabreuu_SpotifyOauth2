package com.example.spotifyapi.auth.data.model

import com.google.gson.annotations.SerializedName


data class SpotifyTokens(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

